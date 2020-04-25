package com.hyjf.web.bank.web.user.paymentauthpage;

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

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.autoup.AutoPlusService;
import com.hyjf.bank.service.user.paymentauthpage.PaymentAuthPageBean;
import com.hyjf.bank.service.user.paymentauthpage.PaymentAuthService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.login.LoginService;
import com.hyjf.web.util.WebUtils;


/**
 * 
 * 服务费授权
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月24日
 * @see 上午9:43:09
 */
@Controller
@RequestMapping(value = PaymentAuthPageDefine.REQUEST_MAPPING)
public class PaymentAuthPageController extends BaseController {

    Logger _log = LoggerFactory.getLogger(PaymentAuthPageController.class);
    
    @Autowired
    private PaymentAuthService paymentAuthService;
    
    @Autowired
    private AutoPlusService autoPlusService;

    @Autowired
    private LoginService loginService;
    
    /**
    * 
    * 服务费授权
    * @author sunss
    * @param request
    * @param response
    * @return
    */
    @Deprecated
    @RequestMapping(PaymentAuthPageDefine.PAYMENT_AUTH_ACTION)
    public ModelAndView userAuthInves(HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(PaymentAuthPageDefine.THIS_CLASS, PaymentAuthPageDefine.PAYMENT_AUTH_ACTION);
        ModelAndView modelAndView = new ModelAndView();
        
        // 用户id
        WebViewUser user = WebUtils.getUser(request);
        if (user == null) {
            modelAndView = new ModelAndView(PaymentAuthPageDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "登录失效，请重新登陆");
            return modelAndView;
        }
        Users users=autoPlusService.getUsers(user.getUserId());
        if (users.getBankOpenAccount()==0) {// 未开户
            modelAndView = new ModelAndView(PaymentAuthPageDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户未开户！");
            return modelAndView;
        }
        
        // 判断用户是否设置过交易密码
        if (users.getIsSetPassword() == 0) {// 未设置交易密码
            modelAndView = new ModelAndView(PaymentAuthPageDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户未设置交易密码");
            return modelAndView;
        }
        
        // 判断是否授权过  
        if(autoPlusService.checkIsAuth(user.getUserId(),BankCallConstant.TXCODE_PAYMENT_AUTH_PAGE)){
            modelAndView = new ModelAndView(PaymentAuthPageDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户已授权,无需重复授权");
            return modelAndView;
        }
       
        // 拼装参数 调用江西银行
        // 同步调用路径
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + 
                PaymentAuthPageDefine.REQUEST_MAPPING + PaymentAuthPageDefine.RETURL_SYN_ACTION + ".do";
        // 异步调用路
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + 
                PaymentAuthPageDefine.REQUEST_MAPPING + PaymentAuthPageDefine.RETURL_ASY_ACTION + ".do";

        // 用户ID
        PaymentAuthPageBean openBean = new PaymentAuthPageBean();
        openBean.setUserId(user.getUserId());
        openBean.setIp(CustomUtil.getIpAddr(request));
        openBean.setAccountId(user.getBankAccount());
        // 同步 异步
        openBean.setRetUrl(retUrl);
        openBean.setNotifyUrl(bgRetUrl);
        // 0：PC  1：微官网  2：Android  3：iOS  4：其他
        openBean.setPlatform("0");
        openBean.setChannel(BankCallConstant.CHANNEL_PC);
        openBean.setForgotPwdUrl(CustomConstants.FORGET_PASSWORD_URL);
        // 跳转到汇付天下画面
        try {
            String orderId = GetOrderIdUtils.getOrderId2(openBean.getUserId());
            openBean.setOrderId(orderId);
            modelAndView = paymentAuthService.getCallbankMV(openBean);
            paymentAuthService.insertUserAuthLog(openBean.getUserId(), orderId,Integer.parseInt(openBean.getPlatform()),"5");
            LogUtil.endLog(PaymentAuthPageController.class.toString(), PaymentAuthPageDefine.PAYMENT_AUTH_ACTION);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView = new ModelAndView(PaymentAuthPageDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "调用银行接口失败！");
            LogUtil.errorLog(PaymentAuthPageController.class.toString(), PaymentAuthPageDefine.PAYMENT_AUTH_ACTION, e);
        }

        return modelAndView;
    }

    /**
     * 
     * 服务费授权
     * @author sunss
     * @param request
     * @param response
     * @param bean
     * @return
     */
    @Deprecated
    @RequestMapping(PaymentAuthPageDefine.RETURL_SYN_ACTION)
    public ModelAndView userAuthInvesReturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {

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
            modelAndView = new ModelAndView(PaymentAuthPageDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", autoPlusService.getBankRetMsg(bean.getRetCode()));
            return modelAndView;
        }
        
        // 出借人签约状态查询
        _log.info("服务费授权同步回调调用查询接口查询状态,userId:"+bean.getLogUserId());
        BankCallBean retBean=autoPlusService.getTermsAuthQuery(Integer.parseInt(bean.getLogUserId()),BankCallConstant.CHANNEL_PC);
        _log.info("服务费授权同步回调调用查询接口查询状态结束  结果为:" + (retBean == null ? "空" : retBean.getRetCode()));
        bean=retBean;
        // 返回失败
        if (retBean!=null&& BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode()) && "1".equals(retBean.getPaymentAuth())) {
            // 成功
            WebViewUser webUser = loginService.getWebViewUserByUserId(Integer.parseInt(bean.getLogUserId()));
            WebUtils.sessionLogin(request, response, webUser);
        } else {
            modelAndView = new ModelAndView(PaymentAuthPageDefine.USER_AUTH_ERROR_PATH);
            return modelAndView;
        }
        modelAndView = new ModelAndView(PaymentAuthPageDefine.USER_AUTH_SUCCESS_PATH);
        modelAndView.addObject("message", "服务费授权成功");
        LogUtil.endLog(PaymentAuthPageDefine.THIS_CLASS, PaymentAuthPageDefine.RETURL_SYN_ACTION, "[服务费授权完成后,回调结束]");
        return modelAndView;
    }


    
    /**
     * 服务费授权异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @Deprecated
    @ResponseBody
    @RequestMapping(PaymentAuthPageDefine.RETURL_ASY_ACTION)
    public String userAuthInvesBgreturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        _log.info("[服务费授权异步回调开始]");
        LogUtil.startLog(PaymentAuthPageDefine.THIS_CLASS, PaymentAuthPageDefine.RETURL_ASY_ACTION, "[服务费授权异步回调开始]");
        bean.convert();
        Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
        // 查询用户开户状态
        Users user = this.autoPlusService.getUsers(userId);
        // 成功
        if (user!=null&&bean != null && (BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE)) 
                && "1".equals(bean.getTxState()))) {
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
        _log.info("[用户服务费授权完成后,回调结束]");
        result.setMessage("服务费授权成功");
        result.setStatus(true);
        return JSONObject.toJSONString(result, true);
    }
}
