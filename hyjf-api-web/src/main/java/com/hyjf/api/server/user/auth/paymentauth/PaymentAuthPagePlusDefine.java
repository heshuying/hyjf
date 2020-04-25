package com.hyjf.api.server.user.auth.paymentauth;

import com.hyjf.base.bean.BaseDefine;

public class PaymentAuthPagePlusDefine extends BaseDefine {
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = PaymentAuthPagePlusServer.class.getName();

    public static final String CONTROLLER_NAME = "ApiWebPaymentAuthPagePlusServer";
	/** 外部服务接口:缴费授权 @RequestMapping */
	public static final String REQUEST_MAPPING = "/server/user/paymentAuthPagePlus";
	/** 缴费授权 @RequestMapping */
	public static final String PAYMENT_AUTH_ACTION = "/page";
	/** 同步回调  */
    public static final String RETURL_SYN_ACTION = "/paymentauthReturn";
    /** 异步回调  */
    public static final String RETURL_ASY_ACTION = "/paymentauthBgreturn";
	
	/**错误页面*/
    public static final String PATH_OPEN_ACCOUNT_PAGE_ERROR = "/bank/user/trusteePay/error";
    
    

}
