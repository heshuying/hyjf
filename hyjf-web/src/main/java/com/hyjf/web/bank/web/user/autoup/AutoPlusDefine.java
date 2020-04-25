package com.hyjf.web.bank.web.user.autoup;

import com.hyjf.web.BaseDefine;

public class AutoPlusDefine extends BaseDefine {
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = AutoPlusController.class.getName();

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/autoplus";
	/** 用户自动出借增强授权 */
    public static final String USER_AUTH_INVES_ACTION = "/userAuthInves";
	/** @RequestMapping值 */
	public static final String USER_AUTH_ACTION = "/init";
	/** @RequestMapping值 */
	public static final String USER_AUTH_TO_ACTION = "bank/user/autoplus/authorization";
    /** 用户授权自动出借同步回调 */
    public static final String USER_AUTH_INVES_RETURN_ACTION = "/userAuthInvesReturn";
    /** 用户授权自动出借异步回调 */
    public static final String USER_AUTH_INVES_BGRETURN_ACTION = "/userAuthInvesBgreturn";
    
    /** 用户授权自动出借成功页面*/
    public static final String USER_AUTH_SUCCESS_PATH = "/bank/user/autoplus/auto-tender-success";
    /** 用户授权自动出借失败页面*/
    public static final String USER_AUTH_ERROR_PATH = "/bank/user/autoplus/auto-tender-fail";
    
    
    
    /** 自动债权转让授权 */
    public static final String USER_CREDIT_AUTH_INVES_ACTION = "/credituserAuthInves";
    /** 自动债权转让同步回调 */
    public static final String USER_CREDIT_AUTH_INVES_RETURN_ACTION = "/credituserAuthInvesReturn";
    /** 自动债权转让异步回调 */
    public static final String USER_CREDIT_AUTH_INVES_BGRETURN_ACTION = "/credituserAuthInvesBgreturn";

    /**前导发送验证码*/
    public static final String SENDCODE_ACTION = "/sendcode";
}
