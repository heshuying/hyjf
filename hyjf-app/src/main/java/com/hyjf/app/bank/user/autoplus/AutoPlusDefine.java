package com.hyjf.app.bank.user.autoplus;

import com.hyjf.app.BaseDefine;

public class AutoPlusDefine extends BaseDefine {
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = "AutoPlusController";
	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/autoplus";

    /** 发送短信验证码 */
    public static final String USER_AUTH_SENDSMS_ACTION = "/sendcode";

    /** 自动投标url获取 */
    public static final String GET_USER_AUTH_INVEST_URL = "/getUserAuthInvesUrl";
    /** 自动债转url获取 */
    public static final String GET_USER_AUTH_CREDIT_URL = "/getUserAuthCreditUrl";


	/** 用户自动出借增强授权 */
    public static final String USER_AUTH_INVES_ACTION = "/userAuthInves";
    
    /** 用户授权自动出借同步回调 */
    public static final String USER_AUTH_INVES_RETURN_ACTION = "/userAuthInvesReturn";
    /** 用户授权自动出借异步回调 */
    public static final String USER_AUTH_INVES_BGRETURN_ACTION = "/userAuthInvesBgreturn";
    
    
    /** 自动债权转让授权 */
    public static final String USER_AUTH_CREDIT_ACTION = "/userAuthCredit";
    /** 自动债权转让同步回调 */
    public static final String USER_AUTH_CREDIT_RETURN_ACTION = "/userAuthCreditReturn";
    /** 自动债权转让异步回调 */
    public static final String USER_AUTH_CREDIT_BGRETURN_ACTION = "/userAuthCreditBgreturn";

}
