/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.web.bank.web.user.auth.payrepayauth;

/**
 * @author dangzw
 * @version PayRepayAuthDefine, v0.1 2018/11/28 17:24
 */
public class PayRepayAuthDefine {

    /** 当前controller名称 */
    public static final String THIS_CLASS = PayRepayAuthController.class.getName();

    /** 缴费、还款二合一授权 类路径 */
    public static final String REQUEST_MAPPING = "/bank/user/auth/payrepayauth";

    /** 缴费、还款二合一授权 */
    public static final String PAYREPAY_AUTH_ACTION = "/auth";

    /** 缴费、还款二合一授权成功页面 */
    public static final String USER_AUTH_SUCCESS_PATH = "/bank/user/repayauth/success";

    /** 缴费、还款二合一授权失败页面 */
    public static final String USER_AUTH_ERROR_PATH = "/bank/user/repayauth/error";

    /** 同步回调 */
    public static final String RETURL_SYN_ACTION = "/payRepayAuthReturn";
    
    /** 异步回调 */
    public static final String RETURL_ASY_ACTION = "/payRepayAuthBgreturn";

    /** 用户修改授权信息失败页面 */
    public static final String AUTH_TENDER_AGAIN_ERROR_PATH = "/bank/user/autoplus/auto-tender-again";
}
