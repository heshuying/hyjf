package com.hyjf.web.bank.web.user.repayauto;

import com.hyjf.web.BaseDefine;

public class RepayAutoDefine extends BaseDefine {
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = RepayAutoController.class.getName();

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/repayauto";
	/** 用户自动出借增强授权 */
    public static final String USER_AUTH_INVES_ACTION = "/userAuthInves";

    /** 用户授权自动出借同步回调 */
    public static final String USER_AUTH_INVES_RETURN_ACTION = "/userAuthInvesReturn";
    /** 用户授权自动出借异步回调 */
    public static final String USER_AUTH_INVES_BGRETURN_ACTION = "/userAuthInvesBgreturn";
    
    /** 用户授权自动出借成功页面*/
    public static final String USER_AUTH_SUCCESS_PATH = "/bank/user/repayauth/success";
    /** 用户授权自动出借失败页面*/
    public static final String USER_AUTH_ERROR_PATH = "/bank/user/repayauth/error";
    
    

}
