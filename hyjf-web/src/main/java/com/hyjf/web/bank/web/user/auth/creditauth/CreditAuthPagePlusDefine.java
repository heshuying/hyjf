package com.hyjf.web.bank.web.user.auth.creditauth;

import com.hyjf.web.BaseDefine;

public class CreditAuthPagePlusDefine extends BaseDefine {
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = CreditAuthPagePlusController.class.getName();

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/auth/creditAuthpageplus";
	/** 还款授权 */
    public static final String CREDIT_AUTH_ACTION = "/page";

    /** 还款授权成功页面*/
    public static final String USER_AUTH_SUCCESS_PATH = "/bank/user/autoplus/auto-tender-success";
    /** 还款授权失败页面*/
    public static final String USER_AUTH_ERROR_PATH = "/bank/user/autoplus/auto-tender-fail";
    
    /** 同步回调  */
    public static final String RETURL_SYN_ACTION = "/creditAuthReturn";
    /** 异步回调  */
    public static final String RETURL_ASY_ACTION = "/creditAuthBgreturn";
    
    

}
