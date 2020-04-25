package com.hyjf.app.bank.user.directrecharge;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseMapBean;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.bank.user.auto.AutoDefine;
import com.hyjf.app.bank.user.paymentauthpage.PaymentAuthPagDefine;
import com.hyjf.app.bank.user.transpassword.TransPasswordDefine;
import com.hyjf.app.util.DES;
import com.hyjf.app.util.SecretUtil;
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

/**
 * 3.3.充值页面
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月26日
 * @see 下午2:29:50
 */
@Controller
@RequestMapping(value = UserDirectRechargeDefine.REQUEST_MAPPING)
public class UserDirectRechargeController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(UserDirectRechargeController.class);

    @Autowired
    private DirectRechargeServer directRechargeServer ;
    
    @Autowired
    private RechargeService userRechargeService;
    @Autowired
    private AuthService authService;

    /**
     * 当前controller名称
     */
    public static final String THIS_CLASS = UserDirectRechargeController.class.getName();

    /**
     * 
     * 调用江西银行进行充值
     * @author sunss
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(UserDirectRechargeDefine.USER_DIRECT_RECHARGE_ACTION)
    public ModelAndView recharge(HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(TransPasswordDefine.THIS_CLASS, UserDirectRechargeDefine.USER_DIRECT_RECHARGE_ACTION);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView = new ModelAndView();
        BaseMapBean baseMapBean=new BaseMapBean();
        String sign = request.getParameter("sign");
        Integer userId = SecretUtil.getUserId(sign); // 用户ID
        String txAmount = request.getParameter("money");// 交易金额
        String cardNo = request.getParameter("cardNo");// 开户银行代号
        String platform = request.getParameter("platform");// 终端类型
        String mobile = request.getParameter("mobile");// 用户的手机号
        String isMencry = request.getParameter("isMencry");// 版本号
        // token
        String token = request.getParameter("token");
        
        if(userId == null || StringUtils.isBlank(mobile) || StringUtils.isBlank(sign) || StringUtils.isBlank(txAmount)){
            return getErrorMV("系统参数错误！",baseMapBean); 
        }
        // 解密手机号  
        // 取得加密用的Key
        logger.info("解密前的手机号["+mobile+"],充值金额:[" + txAmount + "]");
        if(!"1".equals(isMencry)){
            String key = SecretUtil.getKey(sign);
            if (Validator.isNull(key)) {
                return getErrorMV("请求参数非法！",baseMapBean); 
            }
            // 解密
            mobile = DES.decodeValue(key, mobile);
        }
        logger.info("充值手机号为["+mobile+"],充值金额:[" + txAmount + "]");
        Users user = this.directRechargeServer.getUsers(userId);
        if (user.getIsSetPassword() == 0) {// 未设置交易密码
            return getErrorMV("用户未设置交易密码！",baseMapBean); 
        }
        if (user == null || user.getStatus() == 1) {
            return getErrorMV("对不起,该用户已经被禁用！",baseMapBean); 
        }
        if (user.getUserType() == 1) {
            return getErrorMV("对不起,企业用户只能通过线下充值。",baseMapBean); 
        }
        // 检查参数(交易金额是否数字)
        if (Validator.isNull(txAmount) || !NumberUtils.isNumber(txAmount)) {
            return getErrorMV("请输入充值金额。",baseMapBean); 
        }
        // 检查参数(交易金额是否大于0)
        BigDecimal transAmt = new BigDecimal(txAmount);
        if (transAmt.compareTo(BigDecimal.ZERO) < 0) {
            return getErrorMV("充值金额不能为负数。",baseMapBean); 
        }
        if (transAmt.compareTo(new BigDecimal(99999999.99)) > 0) {
            return getErrorMV("充值金额不能大于99,999,999.99元。",baseMapBean); 
        }
        BankOpenAccount account = this.directRechargeServer.getBankOpenAccount(userId);
        if (account == null) {
            return getErrorMV("用户未开户！",baseMapBean); 
        }
        if (Validator.isNull(mobile)) {
            return getErrorMV("手机号不能为空！",baseMapBean); 
        }
        if (Validator.isNull(txAmount)) {
            return getErrorMV("充值金额不能为空！",baseMapBean); 
        }
        // 充值金额校验
        if (!txAmount.matches("-?[0-9]+.*[0-9]*")) {
            logger.info("充值金额格式错误,充值金额:[" + txAmount + "]");
            return getErrorMV("充值金额格式错误！",baseMapBean); 
        }
        //判断小数位数
        if(txAmount.indexOf(".")>=0){
            String l = txAmount.substring(txAmount.indexOf(".")+1,txAmount.length());
            if(l.length()>2){
                logger.info("充值金额不能大于两位小数,充值金额:[" + txAmount + "]");
                return getErrorMV("充值金额不能大于两位小数！",baseMapBean); 
            }
        }
       
        if(!Validator.isMobile(mobile)){
            return getErrorMV("手机号码不正确！",baseMapBean); 
        }
        
        // 根据用户ID查询用户平台银行卡信息
        BankCard bankCard = this.directRechargeServer.getBankCardByUserId(userId);
        if (bankCard == null) {
            logger.info("根据用户ID查询用户银行卡信息失败,用户ID:[" + userId + "].");
            return getErrorMV("查询用户银行卡信息失败！",baseMapBean); 
        }
        // 服务费授权状态和开关
        if (!authService.checkPaymentAuthStatus(userId)) {
            return getErrorMV("未进行服务费授权！",baseMapBean); 
        }
        
        cardNo = bankCard.getCardNo() == null ? "" : bankCard.getCardNo();
        UsersInfo userInfo = this.directRechargeServer.getUsersInfoByUserId(userId);
        String idNo = userInfo.getIdcard();
        String name = userInfo.getTruename();
        try {
            // 同步调用路径
            String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() +  UserDirectRechargeDefine.REQUEST_MAPPING
                    + UserDirectRechargeDefine.RETURL_SYN_ACTION + ".do?txAmount="+transAmt;
            // 异步调用路
            String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath() +  UserDirectRechargeDefine.REQUEST_MAPPING
                    + UserDirectRechargeDefine.RETURL_ASY_ACTION + ".do?phone="+mobile;
            
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
            directRechargeBean.setNotifyUrl(bgRetUrl);
            directRechargeBean.setPlatform(platform);
            directRechargeBean.setChannel(BankCallConstant.CHANNEL_APP);
            directRechargeBean.setAccountId(account.getAccount());
            String forgetPassworedUrl = CustomConstants.FORGET_PASSWORD_URL + "?sign=" + sign + "&token=" + token+"&platform="+platform;
            directRechargeBean.setForgotPwdUrl(forgetPassworedUrl);
            modelAndView = directRechargeServer.insertGetMV(directRechargeBean);
            LogUtil.endLog(THIS_CLASS, UserDirectRechargeDefine.USER_DIRECT_RECHARGE_ACTION);
            return modelAndView;
        } catch (Exception e) {
            logger.error("调用银行接口失败",e);
            LogUtil.errorLog(THIS_CLASS, UserDirectRechargeDefine.USER_DIRECT_RECHARGE_ACTION, e);
            return getErrorMV("调用银行接口失败！",baseMapBean); 
        }
    }

    private ModelAndView getErrorMV(String msg, BaseMapBean baseMapBean) {
        ModelAndView modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, msg);
        baseMapBean.setCallBackAction(CustomConstants.HOST+UserDirectRechargeDefine.JUMP_HTML_FAILED_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
        LogUtil.endLog(THIS_CLASS, UserDirectRechargeDefine.USER_DIRECT_RECHARGE_ACTION);
        return modelAndView;
    }
    
    private ModelAndView getSuccessMV(String msg,String txAmount, BaseMapBean baseMapBean) {
        ModelAndView modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, msg);
        baseMapBean.set("money", txAmount);
        baseMapBean.setCallBackAction(CustomConstants.HOST+UserDirectRechargeDefine.JUMP_HTML_SUCCESS_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
        LogUtil.endLog(THIS_CLASS, UserDirectRechargeDefine.USER_DIRECT_RECHARGE_ACTION);
        return modelAndView;
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
    @RequestMapping(UserDirectRechargeDefine.RETURL_SYN_ACTION)
    public ModelAndView pageReturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {

        LogUtil.startLog(THIS_CLASS, UserDirectRechargeDefine.RETURL_SYN_ACTION, "[充值同步回调开始]");
        String isSuccess = request.getParameter("isSuccess");
        BaseMapBean baseMapBean=new BaseMapBean();
        if (bean == null) {
            return getErrorMV("银行返回为空!",baseMapBean);
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
        
        if (isSuccess != null && "1".equals(isSuccess)) {
            // 成功
            DecimalFormat df = CustomConstants.DF_FOR_VIEW;
            BigDecimal feeAmt = new BigDecimal(bean.getTxAmount());
            return getSuccessMV("充值成功！",df.format(feeAmt).toString(),baseMapBean); 
        }
        
        bean.convert();
        // 银行返回响应代码
        String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
        
        if (bean!=null&& BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode)) {
            // 成功
            DecimalFormat df = CustomConstants.DF_FOR_VIEW;
            BigDecimal feeAmt = new BigDecimal(bean.getTxAmount());
            return getSuccessMV("充值成功！",df.format(feeAmt).toString(),baseMapBean); 
        } else {
            return getErrorMV("失败原因:"+directRechargeServer.getBankRetMsg(bean.getRetCode()),baseMapBean); 
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
    @RequestMapping(UserDirectRechargeDefine.RETURL_ASY_ACTION)
    public BankCallResult bgreturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        LogUtil.startLog(THIS_CLASS, PaymentAuthPagDefine.RETURL_ASY_ACTION, "[缴费授权异步回调开始]");
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
                result.setMessage("充值成功");
                result.setStatus(true);
                // 神策数据统计 add by liuyang 20180725 start
                try {
                    SensorsDataBean sensorsDataBean = new SensorsDataBean();
                    sensorsDataBean.setEventCode("recharge_result");
                    sensorsDataBean.setOrderId(bean.getLogOrderId());
                    sensorsDataBean.setUserId(Integer.parseInt(bean.getLogUserId()));
                    this.directRechargeServer.sendSensorsDataMQ(sensorsDataBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 神策数据统计 add by liuyang 20180725 end
                return result;
            } else {
                result.setMessage("充值失败");
                result.setStatus(false);
                return result;
            }
        }
        LogUtil.endLog(THIS_CLASS, PaymentAuthPagDefine.RETURL_ASY_ACTION, "[用户充值完成后,回调结束]");
        result.setMessage("充值失败");
        result.setStatus(false);
        return result;
    }
    
}