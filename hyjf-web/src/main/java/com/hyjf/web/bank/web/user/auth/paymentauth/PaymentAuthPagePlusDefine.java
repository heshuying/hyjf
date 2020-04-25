package com.hyjf.web.bank.web.user.auth.paymentauth;

import com.hyjf.web.BaseDefine;

public class PaymentAuthPagePlusDefine extends BaseDefine {
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = PaymentAuthPagePlusController.class.getName();

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/auth/paymentauthpageplus";

	/** 缴费授权 */
    public static final String PAYMENT_AUTH_ACTION = "/page";

    /** 用户授权自动出借成功页面*/
    public static final String USER_AUTH_SUCCESS_PATH = "/bank/user/paymentauth/success";

    /** 用户授权自动出借失败页面*/
    public static final String USER_AUTH_ERROR_PATH = "/bank/user/paymentauth/error";
    
    /** 同步回调  */
    public static final String RETURL_SYN_ACTION = "/paymentauthReturn";

    /** 异步回调  */
    public static final String RETURL_ASY_ACTION = "/paymentauthBgreturn";
    
    /** 用户修改授权信息失败页面*/
    public static final String AUTH_TENDER_AGAIN_ERROR_PATH = "/bank/user/autoplus/auto-tender-again";

    /** 授权须知接口*/
    public static final String AUTH_REQUIRE_ACTION = "/authRequirePage";

    /** 授权须知页面*/
    public static final String AUTH_REQUIRE_PATH = "/bank/user/autoplus/authorization";
}
