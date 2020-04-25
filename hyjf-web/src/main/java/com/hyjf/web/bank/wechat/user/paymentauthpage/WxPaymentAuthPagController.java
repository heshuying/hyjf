package com.hyjf.web.bank.wechat.user.paymentauthpage;

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
import com.hyjf.bank.service.user.autoup.AutoPlusService;
import com.hyjf.bank.service.user.paymentauthpage.PaymentAuthPageBean;
import com.hyjf.bank.service.user.paymentauthpage.PaymentAuthService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.web.BaseController;
import com.hyjf.web.bank.web.user.paymentauthpage.PaymentAuthPageDefine;
import com.hyjf.web.bank.wechat.user.withdraw.WithdrawDefine;

/**
 * 
 * 服务费授权 
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月24日
 * @see 上午10:42:30
 */
@Controller(WxPaymentAuthPagDefine.CONTROLLER_NAME)
@RequestMapping(value = WxPaymentAuthPagDefine.REQUEST_MAPPING)
public class WxPaymentAuthPagController extends BaseController {

    @Autowired
    private PaymentAuthService paymentAuthService;
    
    @Autowired
    private AutoPlusService autoPlusService;
	
	/** 当前controller名称 */
	public static final String THIS_CLASS = WxPaymentAuthPagController.class.getName();
	
	Logger _log = LoggerFactory.getLogger(this.getClass());

	
    @RequestMapping(value =WxPaymentAuthPagDefine.PAYMENT_AUTH_ACTION)
    public ModelAndView authPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        // 获取登陆用户userId
        String userId = request.getParameter("userId");
        _log.info("微信请求服务费授权，userid为："+userId);
        //返回结果url，微信端提供
        String callback = request.getParameter("callback");
        try {
            // 验证请求参数
            if (Validator.isNull(userId)) {
                modelAndView = new ModelAndView(returnUrl("1", "用户未登录", callback));
                return modelAndView;
            }
            Users user = this.paymentAuthService.getUsers(Integer.parseInt(userId));
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
            
            // 判断是否授权过  
            if(autoPlusService.checkIsAuth(user.getUserId(),BankCallConstant.TXCODE_PAYMENT_AUTH_PAGE)){
                modelAndView = new ModelAndView(returnUrl("1", "用户已授权,无需重复授权！", callback));
                return modelAndView;
            }
            
            // 拼装参数 调用江西银行
            // 同步调用路径
            String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + WxPaymentAuthPagDefine.REQUEST_MAPPING
                    + WxPaymentAuthPagDefine.RETURL_SYN_ACTION + ".do?callback="+callback;
            // 异步调用路
            String bgRetUrl = PropUtils.getSystem(CustomConstants.HTTP_HYJF_WEB_URL) + WxPaymentAuthPagDefine.REQUEST_MAPPING
                    + WxPaymentAuthPagDefine.RETURL_ASY_ACTION + ".do";
            
            BankOpenAccount account = this.paymentAuthService.getBankOpenAccount(Integer.parseInt(userId));
            PaymentAuthPageBean openBean = new PaymentAuthPageBean();
            openBean.setUserId(user.getUserId());
            openBean.setIp(CustomUtil.getIpAddr(request));
            openBean.setAccountId(account.getAccount());
            // 同步 异步
            openBean.setRetUrl(retUrl);
            openBean.setNotifyUrl(bgRetUrl);
            // 0：PC  1：微官网  2：Android  3：iOS  4：其他
            openBean.setPlatform("1");
            openBean.setChannel(BankCallConstant.CHANNEL_WEI);
            String forgetPassUrl = CustomConstants.WECHAT_FORGET_PASSWORD_URL + "?userId=" + userId + "&callback=" + CustomConstants.WECHAT_FORGET_PASSWORD_CALLBACK_URL;
            openBean.setForgotPwdUrl(forgetPassUrl);
            String orderId = GetOrderIdUtils.getOrderId2(openBean.getUserId());
            openBean.setOrderId(orderId);
            modelAndView = paymentAuthService.getCallbankMV(openBean);
            paymentAuthService.insertUserAuthLog(openBean.getUserId(), orderId,Integer.parseInt(openBean.getPlatform()),"5");
            modelAndView.addObject(WithdrawDefine.STATUS, WithdrawDefine.STATUS_TRUE);
            LogUtil.endLog(THIS_CLASS, WxPaymentAuthPagDefine.PAYMENT_AUTH_ACTION);
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            _log.info("开户异常,异常信息:[" + e.toString() + "]");
            modelAndView = new ModelAndView(returnUrl("1", "开户异常", callback));
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
    @RequestMapping(WxPaymentAuthPagDefine.RETURL_SYN_ACTION)
    public ModelAndView bankOpenReturn(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BankCallBean bean) {
        _log.info("服务费授权同步回调start,请求参数为：【" + JSONObject.toJSONString(bean, true) + "】");
        bean.convert();
        String callback = request.getParameter("callback");
        
        LogUtil.startLog(PaymentAuthPageDefine.THIS_CLASS, PaymentAuthPageDefine.RETURL_SYN_ACTION, "[服务费授权同步回调开始]");
        ModelAndView modelAndView = new ModelAndView();
        bean.convert();
        String frontParams = request.getParameter("frontParams");
        if(StringUtils.isBlank(bean.getRetCode())&&StringUtils.isNotBlank(frontParams)){
            JSONObject jsonParm = JSONObject.parseObject(frontParams);
            if(jsonParm.containsKey("RETCODE")){
                bean.setRetCode(jsonParm.getString("RETCODE"));
            }
        }
        String retCode = bean.getRetCode();
        if(StringUtils.isNotBlank(retCode)&& !BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode)){
            // 根据银行相应代码,查询错误信息
            String retMsg = autoPlusService.getBankRetMsg(retCode);
            _log.info("服务费授权失败,银行返回响应代码:[" + retCode + "],retMsg:[" + retMsg + "]");
            modelAndView = new ModelAndView(returnUrl("1", "服务费授权失败,失败原因:"+retMsg, callback));
            return modelAndView;
        }
        
        // 出借人签约状态查询
        _log.info("服务费授权同步回调调用查询接口查询状态,userId:"+bean.getLogUserId());
        BankCallBean retBean=autoPlusService.getTermsAuthQuery(Integer.parseInt(bean.getLogUserId()),BankCallConstant.CHANNEL_WEI);
        _log.info("服务费授权同步回调调用查询接口查询状态结束  结果为:" + (retBean == null ? "空" : retBean.getRetCode()));
        bean=retBean;
        // 返回失败
        if (retBean!=null&& BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())&& "1".equals(retBean.getPaymentAuth())) {
            modelAndView = new ModelAndView(returnUrl("0", "服务费授权成功", callback));
            return modelAndView;
        } 
        modelAndView = new ModelAndView(returnUrl("1", "服务费授权失败,失败原因:" + autoPlusService.getBankRetMsg(retCode), callback));
        LogUtil.endLog(PaymentAuthPageDefine.THIS_CLASS, PaymentAuthPageDefine.RETURL_SYN_ACTION, "[服务费授权完成后,回调结束]");
        return modelAndView;
    }

    /**
     * 异步回调
     */
    @ResponseBody
    @RequestMapping(WxPaymentAuthPagDefine.RETURL_ASY_ACTION)
    public String bankOpenBgreturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        LogUtil.startLog(PaymentAuthPageDefine.THIS_CLASS, PaymentAuthPageDefine.RETURL_ASY_ACTION, "[服务费授权异步回调开始]");
        bean.convert();
        Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
        // 查询用户开户状态
        Users user = this.autoPlusService.getUsers(userId);
        // 成功
        if (user!=null&&bean != null && BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE)) 
                && "1".equals(bean.getTxState())) {
            try {
                bean.setOrderId(bean.getLogOrderId());
                // 更新签约状态和日志表
                this.autoPlusService.updateUserAuth(Integer.parseInt(bean.getLogUserId()),bean);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.errorLog(PaymentAuthPageDefine.THIS_CLASS, PaymentAuthPageDefine.RETURL_ASY_ACTION, e);
            }
        }
        LogUtil.endLog(PaymentAuthPageDefine.THIS_CLASS, PaymentAuthPageDefine.RETURL_ASY_ACTION, "[用户服务费授权完成后,回调结束]");
        result.setMessage("服务费授权成功");
        result.setStatus(true);
        return JSONObject.toJSONString(result, true);
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
