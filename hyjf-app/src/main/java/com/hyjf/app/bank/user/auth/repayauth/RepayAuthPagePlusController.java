package com.hyjf.app.bank.user.auth.repayauth;

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
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseMapBean;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.app.util.SignValue;
import com.hyjf.bank.service.user.auth.AuthBean;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;

/**
 * 还款授权
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = RepayAuthPagePlusDefine.REQUEST_MAPPING)
public class RepayAuthPagePlusController extends BaseController {

    Logger _log = LoggerFactory.getLogger(RepayAuthPagePlusController.class);

    @Autowired
    private AuthService authService;

    /**
     * 当前controller名称
     */
    public static final String THIS_CLASS = RepayAuthPagePlusController.class.getName();

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

        BaseMapBean baseMapBean = new BaseMapBean();
        String sign = request.getParameter("sign");
        String platform = request.getParameter("platform");
        // 获取sign缓存
        String value = RedisUtils.get(sign);
        if (value == null) {
            return getErrorMV("失败原因：用户未登录！", baseMapBean,sign,platform);
        }
        SignValue signValue = JSON.parseObject(value, SignValue.class);
        if (signValue == null) {
            return getErrorMV("失败原因：用户未登录！", baseMapBean,sign,platform);
        }
        String token = signValue.getToken();
        if (token == null) {
            return getErrorMV("失败原因：用户未登录！", baseMapBean,sign,platform);
        }
        // 判断用户是否登录
        Integer userId = SecretUtil.getUserId(sign);
        if (userId == null || userId <= 0) {
            return getErrorMV("失败原因：用户未登录！", baseMapBean,sign,platform);
        }
        BankOpenAccount account = this.authService.getBankOpenAccount(userId);
        if (account == null) {
            return getErrorMV("失败原因：用户未开户！", baseMapBean,sign,platform);
        }
        Users user = this.authService.getUsers(userId);
        if (user.getIsSetPassword() == 0) {// 未设置交易密码
            return getErrorMV("失败原因：用户未设置交易密码！", baseMapBean,sign,platform);
        }
        // 判断是否授权过
        if (authService.checkIsAuth(user.getUserId(), AuthBean.AUTH_TYPE_REPAY_AUTH)) {
            return getErrorMV("失败原因：用户已授权,无需重复授权！", baseMapBean,sign,platform);
        }

        // 拼装参数 调用江西银行
        // 同步调用路径
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
                + RepayAuthPagePlusDefine.REQUEST_MAPPING + RepayAuthPagePlusDefine.RETURL_SYN_ACTION + ".do?authType="
                + AuthBean.AUTH_TYPE_REPAY_AUTH+"&sign="+sign+"&platform="+platform;
        // 异步调用路
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + request.getContextPath()
                + RepayAuthPagePlusDefine.REQUEST_MAPPING + RepayAuthPagePlusDefine.RETURL_ASY_ACTION + ".do";
        
        String forgetPassworedUrl = CustomConstants.FORGET_PASSWORD_URL + "?sign=" + sign + "&token=" + token+"&platform="+platform;
        UsersInfo usersInfo = authService.getUsersInfoByUserId(user.getUserId());
        // 用户ID
        AuthBean authBean = new AuthBean();
        authBean.setUserId(user.getUserId());
        authBean.setIp(CustomUtil.getIpAddr(request));
        authBean.setAccountId(account.getAccount());
        // 同步 异步
        authBean.setRetUrl(retUrl);
        authBean.setNotifyUrl(bgRetUrl);
        // 0：PC 1：微官网 2：Android 3：iOS 4：其他
        authBean.setPlatform(platform);
        authBean.setAuthType(AuthBean.AUTH_TYPE_REPAY_AUTH);
        authBean.setChannel(BankCallConstant.CHANNEL_APP);
        authBean.setForgotPwdUrl(forgetPassworedUrl);
        authBean.setName(usersInfo.getTruename());
        authBean.setIdNo(usersInfo.getIdcard());
        authBean.setIdentity(usersInfo.getRoleId() + "");
        authBean.setUserType(user.getUserType());
        // 跳转到银行画面
        try {
            String orderId = GetOrderIdUtils.getOrderId2(authBean.getUserId());
            authBean.setOrderId(orderId);
            modelAndView = authService.getCallbankMV(authBean);
            authService.insertUserAuthLog(authBean.getUserId(), orderId, Integer.parseInt(authBean.getPlatform()), "5");
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
     * 
     * @author sss
     * @param request
     * @param response
     * @param bean
     * @return
     */
    @RequestMapping(RepayAuthPagePlusDefine.RETURL_SYN_ACTION)
    public ModelAndView repayauthReturn(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute BankCallBean bean) {

        LogUtil.startLog(THIS_CLASS, RepayAuthPagePlusDefine.RETURL_SYN_ACTION, "[还款授权同步回调开始]");
        BaseMapBean baseMapBean = new BaseMapBean();
        bean.convert();
        String sign = request.getParameter("sign");
        String platform = request.getParameter("platform");
        String retCode = bean.getRetCode();
        if (StringUtils.isNotBlank(retCode) && !BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode)) {
            return getErrorMV("失败原因:" + authService.getBankRetMsg(bean.getRetCode()), baseMapBean,sign,platform);
        }
        String isSuccess = request.getParameter("isSuccess");
        if (!"1".equals(isSuccess)) {
        	return getErrorMV("失败原因:" + authService.getBankRetMsg(bean.getRetCode()), baseMapBean,sign,platform);
        }
        // 出借人签约状态查询
        logger.info("还款授权同步回调调用查询接口查询状态");
        BankCallBean retBean =
                authService.getTermsAuthQuery(Integer.parseInt(bean.getLogUserId()), BankCallConstant.CHANNEL_APP);
        logger.info("还款授权同步回调调用查询接口查询状态结束  结果为:" + (retBean == null ? "空" : retBean.getRetCode()) + "授权状态为："
                + retBean.getRepayAuth());
        
        // 返回失败
        if (retBean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())) {
            // 成功
            if ("1".equals(retBean.getRepayAuth())) {
                try {
                	if(authService.checkDefaultConfig(retBean, AuthBean.AUTH_TYPE_REPAY_AUTH)){
                        return getErrorMV("失败原因:授权期限过短或额度过低，<br>请重新授权！",baseMapBean,sign,platform);
                    }
                    retBean.setOrderId(bean.getLogOrderId());
                    // 更新签约状态和日志表
                    this.authService.updateUserAuth(Integer.parseInt(bean.getLogUserId()), retBean, AuthBean.AUTH_TYPE_REPAY_AUTH);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.errorLog(RepayAuthPagePlusDefine.THIS_CLASS, RepayAuthPagePlusDefine.RETURL_ASY_ACTION, e);
                }
                return getSuccessMV("还款授权成功！", baseMapBean);
            } else {
                return getErrorMV("失败原因:" + authService.getBankRetMsg(retCode), baseMapBean,sign,platform);
            }
        } else {
            return getErrorMV("失败原因:" + authService.getBankRetMsg(retCode), baseMapBean,sign,platform);
        }
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
        LogUtil.startLog(RepayAuthPagePlusDefine.THIS_CLASS, RepayAuthPagePlusDefine.RETURL_ASY_ACTION, "[还款授权异步回调开始]");
        bean.convert();
        Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
        // 查询用户开户状态
        Users user = this.authService.getUsers(userId);
        if(authService.checkDefaultConfig(bean, AuthBean.AUTH_TYPE_REPAY_AUTH)){
          	 _log.info("[用户还款授权完成后,回调结束]");
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

    private ModelAndView getErrorMV(String msg, BaseMapBean baseMapBean,String sign,String platform) {
        ModelAndView modelAndView = new ModelAndView(RepayAuthPagePlusDefine.JUMP_HTML);
        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, msg);
        baseMapBean.set("authType", AuthBean.AUTH_TYPE_REPAY_AUTH);
        baseMapBean.set("sign", sign);
        baseMapBean.set("platform", platform);

        if("失败原因:授权期限过短或额度过低，<br>请重新授权！".equals(msg)){
        	baseMapBean.setCallBackAction(CustomConstants.HOST + RepayAuthPagePlusDefine.AUTH_TENDER_AGAIN_ERROR_PATH);
        }else{
        	baseMapBean.setCallBackAction(CustomConstants.HOST + RepayAuthPagePlusDefine.USER_AUTH_ERROR_PATH);
        }
        
        modelAndView.addObject("callBackForm", baseMapBean);
        LogUtil.endLog(THIS_CLASS, RepayAuthPagePlusDefine.PAYMENT_AUTH_ACTION);
        return modelAndView;
    }

    private ModelAndView getSuccessMV(String msg, BaseMapBean baseMapBean) {
        ModelAndView modelAndView = new ModelAndView(RepayAuthPagePlusDefine.JUMP_HTML);
        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, msg);
        baseMapBean.setCallBackAction(CustomConstants.HOST + RepayAuthPagePlusDefine.USER_AUTH_SUCCESS_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
        LogUtil.endLog(THIS_CLASS, RepayAuthPagePlusDefine.PAYMENT_AUTH_ACTION);
        return modelAndView;
    }
}
