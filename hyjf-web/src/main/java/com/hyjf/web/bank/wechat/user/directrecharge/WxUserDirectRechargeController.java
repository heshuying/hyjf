package com.hyjf.web.bank.wechat.user.directrecharge;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.hyjf.web.BaseController;
import com.hyjf.web.bank.web.user.directrecharge.UserDirectRechargeDefine;

/**
 * 
 * 页面充值
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月24日
 * @see 上午10:42:30
 */
@Controller(WxUserDirectRechargeDefine.CONTROLLER_NAME)
@RequestMapping(value = WxUserDirectRechargeDefine.REQUEST_MAPPING)
public class WxUserDirectRechargeController extends BaseController {

    @Autowired
    private DirectRechargeServer directRechargeServer ;
    
    @Autowired
    private RechargeService userRechargeService;
	
	/** 当前controller名称 */
	public static final String THIS_CLASS = WxUserDirectRechargeController.class.getName();
	
	Logger _log = LoggerFactory.getLogger(this.getClass());

	
    @RequestMapping(value =WxUserDirectRechargeDefine.PAYMENT_AUTH_ACTION)
    public ModelAndView authPage(HttpServletRequest request, HttpServletResponse response,String mobile,String txAmount ) {
        ModelAndView modelAndView = new ModelAndView();
        // 获取登陆用户userId
        String userId = request.getParameter("userId");
        _log.info("微信请求页面充值，userid为："+userId);
        //返回结果url，微信端提供
        String callback = request.getParameter("callback");
        try {
            // 验证请求参数
            if (Validator.isNull(userId)) {
                modelAndView = new ModelAndView(returnUrl("1", "用户未登录", callback));
                return modelAndView;
            }
            Users user = this.directRechargeServer.getUsers(Integer.parseInt(userId));
            if (Validator.isNull(user)) {
                modelAndView = new ModelAndView(returnUrl("1", "获取用户信息失败", callback));
                return modelAndView;
            }
            
            if (user.getBankOpenAccount()==0) {// 未开户
                modelAndView = new ModelAndView(returnUrl("1", "用户未开户！", callback));
                return modelAndView;
            }
            // 判断用户是否设置过交易密码
            if (user.getIsSetPassword() == 0) {// 未设置交易密码
                modelAndView = new ModelAndView(returnUrl("1", "用户未设置交易密码！", callback));
                return modelAndView;
            }
            
         // 根据用户ID查询用户平台银行卡信息
            BankCard bankCard = this.directRechargeServer.getBankCardByUserId(user.getUserId());
            if (bankCard == null) {
                modelAndView = new ModelAndView(UserDirectRechargeDefine.DIRECTRE_CHARGE_ERROR_PATH);
                modelAndView.addObject("message", "查询银行卡信息失败！");
                return modelAndView;
            }
            
            if (Validator.isNull(txAmount)) {
                modelAndView = new ModelAndView(UserDirectRechargeDefine.DIRECTRE_CHARGE_ERROR_PATH);
                modelAndView.addObject("message", "充值金额不能为空！");
                return modelAndView;
            }
            // 充值金额校验
            if (!txAmount.matches("-?[0-9]+.*[0-9]*")) {
                logger.info("充值金额格式错误,充值金额:[" + txAmount + "]");
                modelAndView = new ModelAndView(UserDirectRechargeDefine.DIRECTRE_CHARGE_ERROR_PATH);
                modelAndView.addObject("message", "充值金额格式错误！");
                return modelAndView;
            }
            //判断小数位数
            if(txAmount.indexOf(".")>=0){
                String l = txAmount.substring(txAmount.indexOf(".")+1,txAmount.length());
                if(l.length()>2){
                    logger.info("充值金额不能大于两位小数,充值金额:[" + txAmount + "]");
                    modelAndView = new ModelAndView(UserDirectRechargeDefine.DIRECTRE_CHARGE_ERROR_PATH);
                    modelAndView.addObject("message", "充值值金额不能大于两位小数！");
                    return modelAndView;
                }
            }
            BankOpenAccount account = this.directRechargeServer.getBankOpenAccount(user.getUserId());
            String cardNo = bankCard.getCardNo() == null ? "" : bankCard.getCardNo();
            UsersInfo userInfo = this.directRechargeServer.getUsersInfoByUserId(user.getUserId());
            String idNo = userInfo.getIdcard();
            String name = userInfo.getTruename();
            
            // 拼装参数 调用江西银行
            // 同步调用路径
            String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + WxUserDirectRechargeDefine.REQUEST_MAPPING
                    + WxUserDirectRechargeDefine.RETURL_SYN_ACTION + ".do?txAmount="+txAmount+"&callback="+callback;
            // 异步调用路
            String bgRetUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + WxUserDirectRechargeDefine.REQUEST_MAPPING
                    + WxUserDirectRechargeDefine.RETURL_ASY_ACTION + ".do?phone="+mobile;
            
         // 用户ID
            UserDirectRechargeBean directRechargeBean = new UserDirectRechargeBean();
            directRechargeBean.setTxAmount(txAmount);
            // 身份证信息
            directRechargeBean.setIdNo(idNo);
            directRechargeBean.setName(name);
            directRechargeBean.setCardNo(cardNo);
            directRechargeBean.setMobile(mobile);
            directRechargeBean.setUserId(user.getUserId());
            directRechargeBean.setIp(CustomUtil.getIpAddr(request));
            directRechargeBean.setUserName(user.getUsername());
            // 同步 异步
            directRechargeBean.setRetUrl(retUrl);
            directRechargeBean.setNotifyUrl(bgRetUrl);
            directRechargeBean.setPlatform("1");
            directRechargeBean.setChannel(BankCallConstant.CHANNEL_WEI);
            directRechargeBean.setAccountId(account.getAccount());
            String forgetPassworedUrl = CustomConstants.FORGET_PASSWORD_URL;
            directRechargeBean.setForgotPwdUrl(forgetPassworedUrl);
            modelAndView = directRechargeServer.insertGetMV(directRechargeBean);
            LogUtil.endLog(THIS_CLASS, WxUserDirectRechargeDefine.PAYMENT_AUTH_ACTION);
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            _log.info("页面充值异常,异常信息:[" + e.toString() + "]");
            modelAndView = new ModelAndView(returnUrl("1", "充值异常", callback));
            return modelAndView;
        }
    }
    
    /**
     * 
     * 同步回调
     * @author sunss
     * @param request
     * @param response
     * @param bean
     * @return
     */
    @RequestMapping(WxUserDirectRechargeDefine.RETURL_SYN_ACTION)
    public ModelAndView bankOpenReturn(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BankCallBean bean) {
        _log.info("页面充值同步回调start,请求参数为：【" + JSONObject.toJSONString(bean, true) + "】");
        bean.convert();
        String callback = request.getParameter("callback");
        String frontParams = request.getParameter("frontParams");
        String isSuccess = request.getParameter("isSuccess");
        if(StringUtils.isBlank(bean.getRetCode())&&StringUtils.isNotBlank(frontParams)){
            JSONObject jsonParm = JSONObject.parseObject(frontParams);
            if(jsonParm.containsKey("RETCODE")){
                bean.setRetCode(jsonParm.getString("RETCODE"));
            }
        }
        // 跳转到成功连接了  说明成功了
        if (isSuccess != null && "1".equals(isSuccess)) {
         // 成功
            return new ModelAndView(returnUrl("0", "充值成功", callback));
        }
        
        // 返回失败
        if (bean!=null&& BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
            // 成功
            return new ModelAndView(returnUrl("0", "充值成功", callback));
        } else {
            return new ModelAndView(returnUrl("1", "充值失败,失败原因：" + directRechargeServer.getBankRetMsg(bean.getRetCode()), callback));
        }
    }

    /**
     * 异步回调
     */
    @ResponseBody
    @RequestMapping(WxUserDirectRechargeDefine.RETURL_ASY_ACTION)
    public BankCallResult bankOpenBgreturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        LogUtil.startLog(UserDirectRechargeDefine.THIS_CLASS, UserDirectRechargeDefine.RETURL_ASY_ACTION, "[页面充值异步回调开始]");
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
        if (user!=null&&bean != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))) {
            JSONObject msg = this.userRechargeService.handleRechargeOnlineInfo(bean, params);
            // 充值成功
            if (msg != null && msg.get("error").equals("0")) {
                logger.info("充值成功,手机号:[" + bean.getMobile() + "],用户ID:[" + userId + "],充值金额:[" + bean.getTxAmount() + "]");
                result.setMessage("充值成功");
                result.setStatus(true);
                return result;
            } else {
                result.setMessage("充值失败");
                result.setStatus(false);
                return result;
            }
        }
        LogUtil.endLog(UserDirectRechargeDefine.THIS_CLASS, UserDirectRechargeDefine.RETURL_ASY_ACTION, "[用户充值完成后,回调结束]");
        result.setMessage("充值失败");
        result.setStatus(false);
        return result;
    }
	
	/**
     * 拼接返回结果
     * 
     * @param error
     * @param errorDesc
     * @param callback
     * @return
     */
    public String returnUrl(String error, String errorDesc, String callback) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("error", error);
        map.put("errorDesc", errorDesc);
        String data = JSON.toJSONString(map);
        try {
            data = URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = callback + "backinfo/" + data;
        return "redirect:" + url;
    }
}
