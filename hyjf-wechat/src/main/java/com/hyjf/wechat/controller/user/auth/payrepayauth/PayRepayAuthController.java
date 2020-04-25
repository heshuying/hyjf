/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.wechat.controller.user.auth.payrepayauth;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.auth.AuthBean;
import com.hyjf.bank.service.user.auth.AuthService;
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
import com.hyjf.wechat.BaseResultBeanFrontEnd;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseMapBean;
import com.hyjf.wechat.util.AppUserToken;
import com.hyjf.wechat.util.SecretUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dangzw
 * @version PayRepayAuthController, v0.1 2018/11/28 17:23
 */
@Controller
@RequestMapping(value = PayRepayAuthDefine.REQUEST_MAPPING)
public class PayRepayAuthController extends BaseController {
    Logger _log = LoggerFactory.getLogger(PayRepayAuthController.class);

    @Autowired
    private AuthService authService;

    /**
     * 缴费、还款二合一授权
     * @param request
     * @return
     */
    @SignValidate
    @RequestMapping(PayRepayAuthDefine.PAYREPAY_AUTH_ACTION)
    public ModelAndView auth(HttpServletRequest request) {
        _log.info("缴费、还款二合一授权开始",PayRepayAuthDefine.THIS_CLASS, PayRepayAuthDefine.PAYREPAY_AUTH_ACTION);
        ModelAndView modelAndView;
        BaseMapBean baseMapBean = new BaseMapBean();
        // 获取sign缓存
        String sign = request.getParameter("sign");
        //判断用户是否登录
        Integer userId = requestUtil.getRequestUserId(request);
        if(userId == null){
            AppUserToken token = SecretUtil.getUserId(sign);
            if (token != null) {
                userId = token.getUserId();
            }
        }
        if(userId==null || userId<=0){
            return getErrorMV("失败原因：用户未登录！",baseMapBean,sign);
        }
        BankOpenAccount account = this.authService.getBankOpenAccount(userId);
        if (account == null) {
            return getErrorMV("失败原因：用户未开户！",baseMapBean,sign);
        }
        Users user = this.authService.getUsers(userId);
        if (user.getIsSetPassword() == 0) {// 未设置交易密码
            return getErrorMV("失败原因：用户未设置交易密码！",baseMapBean,sign);
        }
        // 判断是否授权过
        if(authService.checkIsAuth(user.getUserId() ,AuthBean.AUTH_TYPE_PAY_REPAY_AUTH)){
            return getErrorMV("失败原因：用户已授权,无需重复授权！",baseMapBean,sign);
        }

        // 拼装参数 调用江西银行
        // 同步调用路径
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)
                + request.getContextPath()
                + PayRepayAuthDefine.REQUEST_MAPPING
                + PayRepayAuthDefine.RETURL_SYN_ACTION +".page?authType="
                + AuthBean.AUTH_TYPE_PAY_REPAY_AUTH
                + "&sign="+ sign;
        // 异步调用路
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)
                + request.getContextPath()
                + PayRepayAuthDefine.REQUEST_MAPPING
                + PayRepayAuthDefine.RETURL_ASY_ACTION +".do";

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
        authBean.setPlatform(CustomConstants.CLIENT_WECHAT);
        authBean.setAuthType(AuthBean.AUTH_TYPE_PAY_REPAY_AUTH);
        authBean.setChannel(BankCallConstant.CHANNEL_WEI);
        authBean.setForgotPwdUrl(CustomConstants.FORGET_PASSWORD_URL + "?sign=" + sign);
        authBean.setName(usersInfo.getTruename());
        authBean.setIdNo(usersInfo.getIdcard());
        authBean.setIdentity(usersInfo.getRoleId()+"");
        authBean.setUserType(user.getUserType());
        // 跳转到江西银行画面
        try {
            String orderId = GetOrderIdUtils.getOrderId2(authBean.getUserId());
            authBean.setOrderId(orderId);
            modelAndView = authService.getCallbankMV(authBean);
            if(authBean.getPaymentAuthStatus() && authBean.getRepayAuthAuthStatus()){
                //开通缴费授权、还款授权
                authService.insertUserAuthLog(authBean.getUserId(), orderId, Integer.parseInt(authBean.getPlatform()), "15");
            }else if(authBean.getPaymentAuthStatus()){
                //开通缴费授权
                authService.insertUserAuthLog(authBean.getUserId(), orderId, Integer.parseInt(authBean.getPlatform()), "5");
            }else if(authBean.getRepayAuthAuthStatus()){
                //开通还款授权
                authService.insertUserAuthLog(authBean.getUserId(), orderId, Integer.parseInt(authBean.getPlatform()), "6");
            }else{
                //开通缴费授权、还款授权
                authService.insertUserAuthLog(authBean.getUserId(), orderId, Integer.parseInt(authBean.getPlatform()), "15");
            }
        } catch (Exception e) {
            modelAndView = new ModelAndView(PayRepayAuthDefine.USER_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "调用银行接口失败！");
            _log.info("缴费、还款二合一授权、插入日志表异常",
                    "className"+ PayRepayAuthDefine.THIS_CLASS + "methodPath"+ PayRepayAuthDefine.PAYREPAY_AUTH_ACTION,
                    e);
        }
        _log.info("缴费、还款二合一授权结束",PayRepayAuthDefine.THIS_CLASS, PayRepayAuthDefine.PAYREPAY_AUTH_ACTION);
        return modelAndView;
    }

    /**
     * 缴费、还款二合一授权同步回调
     * @param request
     * @param response
     * @param bean
     * @return
     */
    @RequestMapping(PayRepayAuthDefine.RETURL_SYN_ACTION)
    public ModelAndView payRepayAuthReturn(HttpServletRequest request, HttpServletResponse response,
                                        @ModelAttribute BankCallBean bean) {
        _log.info("缴费、还款二合一授权[同步回调]开始",PayRepayAuthDefine.THIS_CLASS, PayRepayAuthDefine.RETURL_SYN_ACTION);
        BaseMapBean baseMapBean = new BaseMapBean();
        bean.convert();
        String sign = request.getParameter("sign");
        String frontParams = request.getParameter("frontParams");
        if(StringUtils.isBlank(bean.getRetCode())&&StringUtils.isNotBlank(frontParams)){
            JSONObject jsonParm = JSONObject.parseObject(frontParams);
            if(jsonParm.containsKey("RETCODE")){
                bean.setRetCode(jsonParm.getString("RETCODE"));
            }
        }
        String isSuccess = request.getParameter("isSuccess");
        if (!"1".equals(isSuccess)) {
            return getErrorMV("失败原因:"+authService.getBankRetMsg(bean.getRetCode()),baseMapBean,sign);
        }
        String retCode = bean.getRetCode();
        if(StringUtils.isNotBlank(retCode)&& !BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode)){
            return getErrorMV("失败原因:"+authService.getBankRetMsg(bean.getRetCode()),baseMapBean,sign);
        }
        // 投资人签约状态查询
        _log.info("缴费、还款二合一授权[同步回调]调用查询接口查询状态,userId:"+ bean.getLogUserId());
        BankCallBean retBean = authService.getTermsAuthQuery(Integer.parseInt(bean.getLogUserId()), BankCallConstant.CHANNEL_WEI);
        _log.info("缴费、还款二合一授权[同步回调]调用查询接口查询状态结束 结果为:"+ (retBean == null ? "空" : retBean.getRetCode())
                + "授权状态为："+ retBean.getPaymentAuth());
        // 返回失败
        if (retBean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())) {
            // 成功
            if("1".equals(retBean.getPaymentAuth())){
                try {
                    _log.info("同步 --->" + bean.getTxCode());
                    if(authService.checkDefaultConfig(retBean,AuthBean.AUTH_TYPE_PAY_REPAY_AUTH)){
                        return getErrorMV("失败原因:授权期限过短或额度过低，<br>请重新授权！" ,baseMapBean ,sign);
                    }
                    retBean.setOrderId(bean.getLogOrderId());
                    // 更新签约状态和日志表
                    this.authService.updateUserAuth(Integer.parseInt(bean.getLogUserId()), retBean ,AuthBean.AUTH_TYPE_PAY_REPAY_AUTH);
                } catch (Exception e) {
                    _log.info("缴费、还款二合一授权[同步回调]更新签约状态和日志表异常",
                            "className"+ PayRepayAuthDefine.THIS_CLASS + "methodPath"+ PayRepayAuthDefine.RETURL_SYN_ACTION,
                            e);
                }
                _log.info("缴费、还款二合一授权完成，[同步回调]结束", PayRepayAuthDefine.THIS_CLASS, PayRepayAuthDefine.RETURL_SYN_ACTION);
                return getSuccessMV("缴费、还款二合一授权成功！" ,baseMapBean);
            }else{
                _log.info("缴费、还款二合一授权失败，[同步回调]结束", PayRepayAuthDefine.THIS_CLASS, PayRepayAuthDefine.RETURL_SYN_ACTION);
                return getErrorMV("失败原因:"+ authService.getBankRetMsg(retCode) ,baseMapBean ,sign);
            }
        } else {
            _log.info("缴费、还款二合一授权失败，[同步回调]结束", PayRepayAuthDefine.THIS_CLASS, PayRepayAuthDefine.RETURL_SYN_ACTION);
            return getErrorMV("失败原因:"+ authService.getBankRetMsg(retCode) ,baseMapBean ,sign);
        }
    }

    /**
     * 缴费、还款二合一授权异步回调
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(PayRepayAuthDefine.RETURL_ASY_ACTION)
    public BankCallResult payRepayAuthBgreturn(@ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        _log.info("缴费、还款二合一授权[异步回调]开始",PayRepayAuthDefine.THIS_CLASS, PayRepayAuthDefine.RETURL_ASY_ACTION);
        bean.convert();
        Integer userId = Integer.parseInt(bean.getLogUserId()); // 用户ID
        // 查询用户开户状态
        Users user = this.authService.getUsers(userId);
        _log.info("异步 --->" + bean.getTxCode());
        if(authService.checkDefaultConfig(bean, AuthBean.AUTH_TYPE_PAY_REPAY_AUTH)){
            _log.info("[用户缴费、还款二合一授权完成，[异步回调]结束]");
            result.setMessage("缴费、还款二合一授权成功");
            result.setStatus(true);
            return result;
        }
        // 成功
        if (user != null && bean != null
                && (BankCallConstant.RESPCODE_SUCCESS.equals(bean.get(BankCallConstant.PARAM_RETCODE)))) {
            try {
                bean.setOrderId(bean.getLogOrderId());
                // 更新签约状态和日志表
                this.authService.updateUserAuth(Integer.parseInt(bean.getLogUserId()), bean, AuthBean.AUTH_TYPE_PAY_REPAY_AUTH);
            } catch (Exception e) {
                _log.info("缴费、还款二合一授权[异步回调]更新签约状态和日志表异常",
                        "className"+ PayRepayAuthDefine.THIS_CLASS + "methodPath"+ PayRepayAuthDefine.RETURL_ASY_ACTION,
                        e);
            }
        }
        _log.info("用户缴费、还款二合一授权完成后,[异步回调]结束",PayRepayAuthDefine.THIS_CLASS, PayRepayAuthDefine.RETURL_ASY_ACTION);
        result.setMessage("缴费、还款二合一授权成功");
        result.setStatus(true);
        return result;
    }

    private ModelAndView getErrorMV(String msg, BaseMapBean baseMapBean,String sign) {
        ModelAndView modelAndView = new ModelAndView(PayRepayAuthDefine.JUMP_HTML);
        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.FAIL);
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, msg);
        baseMapBean.set("authType", PayRepayAuthDefine.AUTH_TYPE);
        baseMapBean.set("sign", sign);
        if("失败原因:授权期限过短或额度过低，<br>请重新授权！".equals(msg)){
            baseMapBean.setCallBackAction(CustomConstants.HOST + PayRepayAuthDefine.AUTH_TENDER_AGAIN_ERROR_PATH);
        }else{
            baseMapBean.setCallBackAction(CustomConstants.HOST + PayRepayAuthDefine.USER_AUTH_ERROR_PATH);
        }
        modelAndView.addObject("callBackForm", baseMapBean);
        _log.info("缴费、还款二合一授权结束", PayRepayAuthDefine.THIS_CLASS, PayRepayAuthDefine.PAYREPAY_AUTH_ACTION);
        return modelAndView;
    }

    private ModelAndView getSuccessMV(String msg, BaseMapBean baseMapBean) {
        ModelAndView modelAndView = new ModelAndView(PayRepayAuthDefine.JUMP_HTML);
        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
        baseMapBean.set(CustomConstants.APP_STATUS_DESC, msg);
        baseMapBean.set("authType", PayRepayAuthDefine.AUTH_TYPE);
        baseMapBean.setCallBackAction(CustomConstants.HOST + PayRepayAuthDefine.USER_AUTH_SUCCESS_PATH);
        modelAndView.addObject("callBackForm", baseMapBean);
        _log.info("缴费、还款二合一授权结束", PayRepayAuthDefine.THIS_CLASS, PayRepayAuthDefine.RETURL_SYN_ACTION);
        return modelAndView;
    }
}
