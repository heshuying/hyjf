package com.hyjf.web.bank.web.user.auth.repayauth;

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
import com.hyjf.bank.service.user.auth.AuthBean;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.login.LoginService;
import com.hyjf.web.util.WebUtils;

/**
 * 还款授权
 * 
 * @author sunss
 *
 */
@Controller
@RequestMapping(value = RepayAuthPagePlusDefine.REQUEST_MAPPING)
public class RepayAuthPagePlusController extends BaseController {

    Logger _log = LoggerFactory.getLogger(RepayAuthPagePlusController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private LoginService loginService;

    /**
     * 
     * 还款授权
     * 
     * @author sss
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(RepayAuthPagePlusDefine.PAYMENT_AUTH_ACTION)
    public ModelAndView page(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(RepayAuthPagePlusDefine.THIS_CLASS, RepayAuthPagePlusDefine.PAYMENT_AUTH_ACTION);
        ModelAndView modelAndView = new ModelAndView();
        // 用户id
        WebViewUser user = WebUtils.getUser(request);
        if (user == null) {
            modelAndView = new ModelAndView(RepayAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "登录失效，请重新登陆");
            return modelAndView;
        }
        Users users = authService.getUsers(user.getUserId());
        if (users.getBankOpenAccount() == 0) {// 未开户
            modelAndView = new ModelAndView(RepayAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户未开户！");
            return modelAndView;
        }

        // 判断用户是否设置过交易密码
        if (users.getIsSetPassword() == 0) {// 未设置交易密码
            modelAndView = new ModelAndView(RepayAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户未设置交易密码");
            return modelAndView;
        }

        // 判断是否授权过
        if (authService.checkIsAuth(user.getUserId(), AuthBean.AUTH_TYPE_REPAY_AUTH)) {
            modelAndView = new ModelAndView(RepayAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户已授权,无需重复授权");
            return modelAndView;
        }

        // 拼装参数 调用江西银行
        // 同步调用路径
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + RepayAuthPagePlusDefine.REQUEST_MAPPING
                + RepayAuthPagePlusDefine.RETURL_SYN_ACTION + ".do?authType=" + AuthBean.AUTH_TYPE_REPAY_AUTH;
        // 异步调用路
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + RepayAuthPagePlusDefine.REQUEST_MAPPING
                + RepayAuthPagePlusDefine.RETURL_ASY_ACTION + ".do";

        UsersInfo usersInfo = authService.getUsersInfoByUserId(users.getUserId());
        // 用户ID
        AuthBean authBean = new AuthBean();
        authBean.setUserId(user.getUserId());
        authBean.setIp(CustomUtil.getIpAddr(request));
        authBean.setAccountId(user.getBankAccount());
        // 同步 异步
        authBean.setRetUrl(retUrl);
        authBean.setNotifyUrl(bgRetUrl);
        // 0：PC 1：微官网 2：Android 3：iOS 4：其他
        authBean.setPlatform(CustomConstants.CLIENT_PC);
        authBean.setAuthType(AuthBean.AUTH_TYPE_REPAY_AUTH);
        authBean.setChannel(BankCallConstant.CHANNEL_PC);
        authBean.setForgotPwdUrl(CustomConstants.FORGET_PASSWORD_URL);
        authBean.setName(usersInfo.getTruename());
        authBean.setIdNo(usersInfo.getIdcard());
        authBean.setIdentity(usersInfo.getRoleId() + "");
        authBean.setUserType(users.getUserType());
        // 跳转到江西银行画面
        try {
            String orderId = GetOrderIdUtils.getOrderId2(authBean.getUserId());
            authBean.setOrderId(orderId);
            modelAndView = authService.getCallbankMV(authBean);
            authService.insertUserAuthLog(authBean.getUserId(), orderId, Integer.parseInt(authBean.getPlatform()), "6");
            LogUtil.endLog(RepayAuthPagePlusController.class.toString(), RepayAuthPagePlusDefine.PAYMENT_AUTH_ACTION);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView = new ModelAndView(RepayAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "调用银行接口失败！");
            LogUtil.errorLog(RepayAuthPagePlusController.class.toString(), RepayAuthPagePlusDefine.PAYMENT_AUTH_ACTION,
                    e);
        }
        return modelAndView;
    }

    /**
     * 
     * 还款授权
     * @author sss
     * @param request
     * @param response
     * @param bean
     * @return
     */
    @RequestMapping(RepayAuthPagePlusDefine.RETURL_SYN_ACTION)
    public ModelAndView repayauthReturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute BankCallBean bean) {

        LogUtil.startLog(RepayAuthPagePlusDefine.THIS_CLASS, RepayAuthPagePlusDefine.RETURL_SYN_ACTION,
                "[还款授权同步回调开始]");
        ModelAndView modelAndView = new ModelAndView();
        bean.convert();
        String retCode = bean.getRetCode();
        if (StringUtils.isNotBlank(retCode) && !BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode)) {
            modelAndView = new ModelAndView(RepayAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", authService.getBankRetMsg(bean.getRetCode()));
            return modelAndView;
        }
        String isSuccess = request.getParameter("isSuccess");
        if (!"1".equals(isSuccess)) {
        	modelAndView = new ModelAndView(RepayAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
            return modelAndView;
        }
        // 出借人签约状态查询
        _log.info("还款授权同步回调调用查询接口查询状态,userId:" + bean.getLogUserId());
        BankCallBean retBean =
                authService.getTermsAuthQuery(Integer.parseInt(bean.getLogUserId()), BankCallConstant.CHANNEL_PC);
        _log.info("还款授权同步回调调用查询接口查询状态结束  结果为:" + (retBean == null ? "空" : retBean.getRetCode()));
        
        // 返回失败
        if (retBean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())
                && "1".equals(retBean.getRepayAuth())) {
            try {
            	if(authService.checkDefaultConfig(retBean, AuthBean.AUTH_TYPE_REPAY_AUTH)){
                	modelAndView = new ModelAndView(RepayAuthPagePlusDefine.AUTH_TENDER_AGAIN_ERROR_PATH);
                    modelAndView.addObject("message", "授权期限过短或额度过低，<br>请重新授权！");
                    return modelAndView;
                }
                retBean.setOrderId(bean.getLogOrderId());
                // 更新签约状态和日志表
                this.authService.updateUserAuth(Integer.parseInt(bean.getLogUserId()), retBean, AuthBean.AUTH_TYPE_REPAY_AUTH);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.errorLog(RepayAuthPagePlusDefine.THIS_CLASS, RepayAuthPagePlusDefine.RETURL_ASY_ACTION, e);
            }
            // 成功
            WebViewUser webUser = loginService.getWebViewUserByUserId(Integer.parseInt(bean.getLogUserId()));
            WebUtils.sessionLogin(request, response, webUser);
        } else {
            modelAndView = new ModelAndView(RepayAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
            return modelAndView;
        }
        modelAndView = new ModelAndView(RepayAuthPagePlusDefine.USER_AUTH_SUCCESS_PATH);
        modelAndView.addObject("message", "还款授权成功");
        LogUtil.endLog(RepayAuthPagePlusDefine.THIS_CLASS, RepayAuthPagePlusDefine.RETURL_SYN_ACTION,
                "[还款授权完成后,回调结束]");
        return modelAndView;
    }

    /**
     * 还款授权异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(RepayAuthPagePlusDefine.RETURL_ASY_ACTION)
    public BankCallResult repayauthBgreturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        _log.info("[还款授权异步回调开始]");
        LogUtil.startLog(RepayAuthPagePlusDefine.THIS_CLASS, RepayAuthPagePlusDefine.RETURL_ASY_ACTION,
                "[还款授权异步回调开始]");
        bean.convert();
        Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
        // 查询用户开户状态
        Users user = this.authService.getUsers(userId);
        if(authService.checkDefaultConfig(bean, AuthBean.AUTH_TYPE_REPAY_AUTH)){
          	 _log.info("[用户还款授权完成后,回调结束]");
          	 _log.info(JSONObject.toJSONString(bean));
          	 result.setMessage("授权成功");
          	 result.setStatus(true);
          	 return result;
         }
        // 成功
        if (user != null && bean != null
                && (BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE))
                        && "1".equals(bean.getRepayAuth()))) {
            try {
                bean.setOrderId(bean.getLogOrderId());
                // 更新签约状态和日志表
                this.authService.updateUserAuth(Integer.parseInt(bean.getLogUserId()), bean, AuthBean.AUTH_TYPE_REPAY_AUTH);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.errorLog(RepayAuthPagePlusDefine.THIS_CLASS, RepayAuthPagePlusDefine.RETURL_ASY_ACTION, e);
            }
        }
        LogUtil.endLog(RepayAuthPagePlusDefine.THIS_CLASS, RepayAuthPagePlusDefine.RETURL_ASY_ACTION,
                "[用户还款授权完成后,回调结束]");
        _log.info("[用户还款授权完成后,回调结束]");
        result.setMessage("还款授权成功");
        result.setStatus(true);
        return result;
    }
}
