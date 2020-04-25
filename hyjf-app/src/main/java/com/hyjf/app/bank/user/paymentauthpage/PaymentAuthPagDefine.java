package com.hyjf.app.bank.user.paymentauthpage;

import com.hyjf.app.BaseDefine;

public class PaymentAuthPagDefine extends BaseDefine{

    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/bank/user/paymentAuth";
    /** 缴费授权页面 */
    public static final String PAYMENT_AUTH_PAG_ACTION = "/authPage";

    /** 同步@RequestMapping值 */
    public static final String RETURL_SYN_ACTION = "/paymentAuthReturn";
    /** 异步@RequestMapping值 */
    public static final String RETURL_ASY_ACTION = "/paymentAuthBgreturn";
    
    //合规接口改造 start
    /** 缴费授权验证失败*/
    public static final String JUMP_HTML_FAILED_PATH = "/user/setting/paymentauth/result/failed";
    /** 缴费授权处理中*/
    public static final String JUMP_HTML_HANDLING_PATH = "/user/setting/paymentauth/result/handling";
    /** 缴费授权成功*/
    public static final String JUMP_HTML_SUCCESS_PATH = "/user/setting/paymentauth/result/success";

    //合规接口改造 end
}
