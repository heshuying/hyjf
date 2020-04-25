package com.hyjf.app.bank.user.auth.repayauth;

import com.hyjf.app.BaseDefine;


public class RepayAuthPagePlusDefine extends BaseDefine {
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = RepayAuthPagePlusController.class.getName();

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/auth/repayauthpageplus";
	/** 还款授权 */
    public static final String PAYMENT_AUTH_ACTION = "/page";

    /** 用户还款授权成功页面*/
    public static final String USER_AUTH_SUCCESS_PATH = "/user/setting/repayauth/result/success";
    /** 用户还款授权失败页面*/
    public static final String USER_AUTH_ERROR_PATH = "/user/setting/repayauth/result/failed";
    
    /** 同步回调  */
    public static final String RETURL_SYN_ACTION = "/repayauthReturn";
    /** 异步回调  */
    public static final String RETURL_ASY_ACTION = "/repayauthBgreturn";
    
    /** 用户修改授权信息失败页面*/
    public static final String AUTH_TENDER_AGAIN_ERROR_PATH = "/user/setting/quota/result/failed";

}
