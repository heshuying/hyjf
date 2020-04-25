package com.hyjf.web.bank.web.user.auth.invesauth;

import com.hyjf.web.BaseDefine;

public class InvesAuthPagePlusDefine extends BaseDefine {
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = InvesAuthPagePlusController.class.getName();

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/auth/invesauthpageplus";
	/** 还款授权 */
    public static final String INVES_AUTH_ACTION = "/page";

    /** 还款授权成功页面*/
    public static final String USER_AUTH_SUCCESS_PATH = "/bank/user/autoplus/auto-tender-success";
    /** 还款授权失败页面*/
    public static final String USER_AUTH_ERROR_PATH = "/bank/user/autoplus/auto-tender-fail";
    
    /** 用户修改授权信息失败页面*/
    public static final String AUTH_TENDER_AGAIN_ERROR_PATH = "/bank/user/autoplus/auto-tender-again";
    /** 同步回调  */
    public static final String RETURL_SYN_ACTION = "/invesAuthReturn";
    /** 异步回调  */
    public static final String RETURL_ASY_ACTION = "/invesAuthBgreturn";
    
    

}
