/**
 * 设置交易密码
 */
package com.hyjf.web.bank.web.user.auto;

import com.hyjf.web.BaseDefine;

public class AutoDefine extends BaseDefine {
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = AutoController.class.getName();

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/auto";
	/** 用户授权自动出借 */
    public static final String USER_AUTH_INVES_ACTION = "/userAuthInves";
    
    /** 用户授权自动出借同步回调 */
    public static final String USER_AUTH_INVES_RETURN_ACTION = "/userAuthInvesReturn";
    /** 用户授权自动出借异步回调 */
    public static final String USER_AUTH_INVES_BGRETURN_ACTION = "/userAuthInvesBgreturn";
    
    /** 用户授权自动出借成功页面*/
    public static final String USER_AUTH_SUCCESS_PATH = "/bank/user/auth/success";
    /** 用户授权自动出借失败页面*/
    public static final String USER_AUTH_ERROR_PATH = "/bank/user/auth/error";
}
