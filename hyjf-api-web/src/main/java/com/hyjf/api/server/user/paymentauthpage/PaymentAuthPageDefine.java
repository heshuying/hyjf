package com.hyjf.api.server.user.paymentauthpage;

import com.hyjf.base.bean.BaseDefine;

public class PaymentAuthPageDefine extends BaseDefine {
    
    public static final String CONTROLLER_NAME = "ApiWebPaymentAuthPageServer";
	/** 外部服务接口:缴费授权 @RequestMapping */
	public static final String REQUEST_MAPPING = "/server/user/paymentAuthPage";
	/** 缴费授权 @RequestMapping */
	public static final String PAYMENT_AUTH_ACTION = "/page";
	/** 同步回调  */
    public static final String RETURL_SYN_ACTION = "/paymentauthReturn";
    /** 异步回调  */
    public static final String RETURL_ASY_ACTION = "/paymentauthBgreturn";
	
	/** 类名 */
	public static final String THIS_CLASS = PaymentAuthPageServer.class.getName();
	
	/**错误页面*/
    public static final String PATH_OPEN_ACCOUNT_PAGE_ERROR = "/bank/user/trusteePay/error";

}
