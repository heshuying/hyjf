package com.hyjf.wechat.controller.user.directrecharge;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.bank.service.user.directrecharge.DirectRechargeServer;
import com.hyjf.bank.service.user.directrecharge.UserDirectRechargeBean;
import com.hyjf.bank.service.user.recharge.RechargeService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseMapBean;
import com.hyjf.wechat.controller.user.bankopen.OpenAccountDefine;
import com.hyjf.wechat.util.ResultEnum;

/**
 * 充值
 * @author sss
 *
 */
@Controller
@RequestMapping(value = WxUserDirectRechargeDefine.REQUEST_MAPPING)
public class WxUserDirectRechargeController extends BaseController {

	 /**
     * 当前controller名称
     */
    public static final String THIS_CLASS = WxUserDirectRechargeController.class.getName();
    
    private Logger logger = LoggerFactory.getLogger(WxUserDirectRechargeController.class);

    @Autowired
    private DirectRechargeServer directRechargeServer ;
    
    @Autowired
    private RechargeService userRechargeService;

    @Autowired
    private AuthService authService;

    /**
     * 
     * 调用江西银行进行充值
     * @author sunss
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(WxUserDirectRechargeDefine.DIRECT_RECHARGE_ACCOUNT_ACTION)
    @SignValidate
    public ModelAndView recharge(HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(THIS_CLASS, WxUserDirectRechargeDefine.DIRECT_RECHARGE_ACCOUNT_ACTION);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView = new ModelAndView();
        String sign = request.getParameter("sign");
        Integer userId = requestUtil.getRequestUserId(request); // 用户ID
        String txAmount = request.getParameter("money");// 交易金额
        String cardNo = request.getParameter("cardNo");// 开户银行代号
        String platform = "1";
        String mobile = request.getParameter("mobile");// 用户的手机号
        // 参数错误
        if(userId == null || StringUtils.isBlank(mobile) || StringUtils.isBlank(txAmount)){
			return getErrorModelAndView(ResultEnum.PARAM, null);
        }
        logger.info("充值手机号为["+mobile+"],充值金额:[" + txAmount + "]");
        Users user = this.directRechargeServer.getUsers(userId);
        if (user != null && user.getIsSetPassword() == 0) {// 未设置交易密码
        	return getErrorModelAndView(ResultEnum.USER_ERROR_201, null);
        }
        if (user == null || user.getStatus() == 1) {
        	return getErrorModelAndView(ResultEnum.ERROR_996, null);
        }
        if (user.getUserType() == 1) {
        	return getErrorModelAndView(ResultEnum.PARAM, "对不起,企业用户只能通过线下充值。");
        }
        // 检查参数(交易金额是否数字)
        if (Validator.isNull(txAmount) || !NumberUtils.isNumber(txAmount)) {
        	return getErrorModelAndView(ResultEnum.PARAM, "请输入充值金额。");
        }
        // 检查参数(交易金额是否大于0)
        BigDecimal transAmt = new BigDecimal(txAmount);
        if (transAmt.compareTo(BigDecimal.ZERO) < 0) {
        	return getErrorModelAndView(ResultEnum.PARAM, "充值金额不能为负数。");
        }
        if (transAmt.compareTo(new BigDecimal(99999999.99)) > 0) {
        	return getErrorModelAndView(ResultEnum.PARAM, "充值金额不能大于99,999,999.99元。");
        }
        BankOpenAccount account = this.directRechargeServer.getBankOpenAccount(userId);
        if (account == null) {
        	return getErrorModelAndView(ResultEnum.PARAM, "用户未开户！");
        }
        if (Validator.isNull(mobile)) {
        	return getErrorModelAndView(ResultEnum.PARAM, "手机号不能为空！");
        }
        if (Validator.isNull(txAmount)) {
        	return getErrorModelAndView(ResultEnum.PARAM, "充值金额不能为空！");
        }
        // 充值金额校验
        if (!txAmount.matches("-?[0-9]+.*[0-9]*")) {
            return getErrorModelAndView(ResultEnum.PARAM, "充值金额格式错误！");
        }
        //判断小数位数
        if(txAmount.indexOf(".")>=0){
            String l = txAmount.substring(txAmount.indexOf(".")+1,txAmount.length());
            if(l.length()>2){
                logger.info("充值金额不能大于两位小数,充值金额:[" + txAmount + "]");
                return getErrorModelAndView(ResultEnum.PARAM, "充值金额不能大于两位小数！");
            }
        }
        // 服务费授权校验
        if (!authService.checkPaymentAuthStatus(userId)) {
            return getErrorModelAndView(ResultEnum.PARAM, ResultEnum.USER_WITHDRAW_009.getStatusDesc());
        }
        if(!Validator.isMobile(mobile)){
        	return getErrorModelAndView(ResultEnum.PARAM, "手机号码不正确！");
        }
        
        // 根据用户ID查询用户平台银行卡信息
        BankCard bankCard = this.directRechargeServer.getBankCardByUserId(userId);
        if (bankCard == null) {
            logger.info("根据用户ID查询用户银行卡信息失败,用户ID:[" + userId + "].");
            return getErrorModelAndView(ResultEnum.PARAM, "查询用户银行卡信息失败！");
        }
        cardNo = bankCard.getCardNo() == null ? "" : bankCard.getCardNo();
        UsersInfo userInfo = this.directRechargeServer.getUsersInfoByUserId(userId);
        String idNo = userInfo.getIdcard();
        String name = userInfo.getTruename();
        try {
            // 同步调用路径
            String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() +  WxUserDirectRechargeDefine.REQUEST_MAPPING
                    + WxUserDirectRechargeDefine.RETURL_SYN_ACTION + ".page?sign="+sign+"&txAmount="+transAmt;
            // 异步调用路
            String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() +  WxUserDirectRechargeDefine.REQUEST_MAPPING
                    + WxUserDirectRechargeDefine.RETURL_ASY_ACTION + ".do?phone="+mobile;
            
            // 用户ID
            UserDirectRechargeBean directRechargeBean = new UserDirectRechargeBean();
            directRechargeBean.setTxAmount(txAmount);
            // 身份证信息
            directRechargeBean.setIdNo(idNo);
            directRechargeBean.setName(name);
            directRechargeBean.setCardNo(cardNo);
            directRechargeBean.setMobile(mobile);
            directRechargeBean.setUserId(userId);
            directRechargeBean.setIp(CustomUtil.getIpAddr(request));
            directRechargeBean.setUserName(user.getUsername());
            // 同步 异步
            directRechargeBean.setRetUrl(retUrl);
            directRechargeBean.setSuccessfulUrl(retUrl+"&isSuccess=1");
            directRechargeBean.setNotifyUrl(bgRetUrl);
            directRechargeBean.setPlatform(platform);
            directRechargeBean.setChannel(BankCallConstant.CHANNEL_WEI);
            directRechargeBean.setAccountId(account.getAccount());
            String forgetPassworedUrl = CustomConstants.FORGET_PASSWORD_URL + "?sign=" + sign;
            directRechargeBean.setForgotPwdUrl(forgetPassworedUrl);
            modelAndView = directRechargeServer.insertGetMV(directRechargeBean);
            LogUtil.endLog(THIS_CLASS, WxUserDirectRechargeDefine.DIRECT_RECHARGE_ACCOUNT_ACTION);
            return modelAndView;
        } catch (Exception e) {
            logger.error("调用银行接口失败",e);
            LogUtil.errorLog(THIS_CLASS, WxUserDirectRechargeDefine.DIRECT_RECHARGE_ACCOUNT_ACTION, e);
            return getErrorModelAndView(ResultEnum.PARAM, "调用银行接口失败！");
        }
    }

    /**
     * 
     * 充值同步
     * @author sunss
     * @param request
     * @param response
     * @param bean
     * @return
     */
    @SignValidate
    @RequestMapping(WxUserDirectRechargeDefine.RETURL_SYN_ACTION)
    public ModelAndView pageReturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {

        LogUtil.startLog(THIS_CLASS, WxUserDirectRechargeDefine.RETURL_SYN_ACTION, "[充值同步回调开始]");
        String isSuccess = request.getParameter("isSuccess");
        BaseMapBean baseMapBean=new BaseMapBean();
        if (bean == null) {
        	return getErrorModelAndView(ResultEnum.PARAM, "银行返回为空！");
        }
        String money = request.getParameter("txAmount");
        String frontParams = request.getParameter("frontParams");
        if(StringUtils.isBlank(bean.getRetCode())&&StringUtils.isNotBlank(frontParams)){
            JSONObject jsonParm = JSONObject.parseObject(frontParams);
            if(jsonParm.containsKey("RETCODE")){
                bean.setRetCode(jsonParm.getString("RETCODE"));
            }
        }
        bean.setTxAmount(money);
        
        bean.convert();
        if (isSuccess != null && "1".equals(isSuccess)) {
            // 成功
            DecimalFormat df = CustomConstants.DF_FOR_VIEW;
            BigDecimal txAmount = new BigDecimal(bean.getTxAmount());
            ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.JUMP_HTML);
            baseMapBean.set(CustomConstants.APP_STATUS, ResultEnum.SUCCESS.getStatus());
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "充值金额:"+df.format(txAmount).toString()+"元");
            baseMapBean.set("money", df.format(txAmount).toString());
            baseMapBean.setCallBackAction(CustomConstants.HOST + WxUserDirectRechargeDefine.DIRECT_RECHARGE_SUCCESS_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            LogUtil.endLog(THIS_CLASS, WxUserDirectRechargeDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
            return modelAndView;
        }
        // 银行返回响应代码
        String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
        
        if (bean!=null&& BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode)) {
            // 成功
        	DecimalFormat df = CustomConstants.DF_FOR_VIEW;
            BigDecimal txAmount = new BigDecimal(bean.getTxAmount());
            ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.JUMP_HTML);
            baseMapBean.set(CustomConstants.APP_STATUS, ResultEnum.SUCCESS.getStatus());
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "充值金额:"+df.format(txAmount).toString()+"元");
            baseMapBean.set("money", df.format(txAmount).toString());
            baseMapBean.setCallBackAction(CustomConstants.HOST + WxUserDirectRechargeDefine.DIRECT_RECHARGE_ERROR_PATH);
            modelAndView.addObject("callBackForm", baseMapBean);
            LogUtil.endLog(THIS_CLASS, WxUserDirectRechargeDefine.RETURL_SYN_ACTION, "[交易完成后,回调结束]");
            return modelAndView;
        } else {
        	return getErrorModelAndView(ResultEnum.PARAM, directRechargeServer.getBankRetMsg(bean.getRetCode()));
        }
    }

    /**
     * 充值异步
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(WxUserDirectRechargeDefine.RETURL_ASY_ACTION)
    public BankCallResult bgreturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        LogUtil.startLog(THIS_CLASS, WxUserDirectRechargeDefine.RETURL_ASY_ACTION, "[充值异步回调开始]");
        String phone = request.getParameter("phone");
        bean.setMobile(phone);
        bean.convert();
        Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
        // 查询用户开户状态
        Users user = this.directRechargeServer.getUsers(userId);
        Map<String, String> params = new HashMap<String, String>();
        params.put("ip", bean.getUserIP());
        params.put("mobile",bean.getMobile());
        // 成功
        JSONObject msg = this.userRechargeService.handleRechargeOnlineInfo(bean, params);
        if (user!=null&&bean != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
            // 充值成功
            if (msg != null && msg.get("error").equals("0")) {
                logger.info("充值成功,手机号:[" + bean.getMobile() + "],用户ID:[" + userId + "],充值金额:[" + bean.getTxAmount() + "]");
              //  CommonSoaUtils.listedTwoRecharge(userId,new BigDecimal(bean.getTxAmount()));
                result.setMessage("充值成功");
                result.setStatus(true);
                try{
                    // 神策数据统计 add by liuyang 20180726 start
                    //  充值成功后,发送MQ
                    SensorsDataBean sensorsDataBean = new SensorsDataBean();
                    sensorsDataBean.setUserId(userId);
                    sensorsDataBean.setEventCode("recharge_result");
                    sensorsDataBean.setOrderId(bean.getLogOrderId());
                    // 发送神策数据统计MQ
                    this.userRechargeService.sendSensorsDataMQ(sensorsDataBean);
                    // 神策数据统计 add by liuyang 20180726 start
                }catch (Exception e){
                    e.printStackTrace();
                }
                // 神策数据统计 add by liuyang 20180726 end
                return result;
            } else {
                result.setMessage("充值失败");
                result.setStatus(false);
                return result;
            }
        }
        LogUtil.endLog(THIS_CLASS, WxUserDirectRechargeDefine.RETURL_ASY_ACTION, "[用户充值完成后,回调结束]");
        result.setMessage("充值失败");
        result.setStatus(false);
        return result;
    }
    
    private ModelAndView getErrorModelAndView(ResultEnum param,String desc) {
        ModelAndView modelAndView = new ModelAndView(WxUserDirectRechargeDefine.JUMP_HTML);
        BaseMapBean baseMapBean = new BaseMapBean();
        baseMapBean.set(CustomConstants.APP_STATUS, param.getStatus());
        if (desc == null) {
			 baseMapBean.set(CustomConstants.APP_STATUS_DESC, param.getStatusDesc());
		}else{
			baseMapBean.set(CustomConstants.APP_STATUS_DESC, desc);
		}
        baseMapBean.setCallBackAction(CustomConstants.HOST + WxUserDirectRechargeDefine.DIRECT_RECHARGE_ERROR_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
        return modelAndView;
    }
}

