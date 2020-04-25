/**
 * 设置交易密码
 */
package com.hyjf.app.bank.user.auto;

import com.hyjf.app.BaseDefine;

public class AutoDefine extends BaseDefine {
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = AppAutoController.class.getName();

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/auto";
	/** 用户授权自动出借 */
    public static final String USER_AUTH_INVES_ACTION = "/userAuthInves";
    /** 用户授权自动出借状态查询 */
    public static final String GET_USER_AUTO_STATUS_BY_USERID_ACTION = "/getUserAutoStatusByUserId";
    
    /** 用户授权自动出借同步回调 */
    public static final String USER_AUTH_INVES_RETURN_ACTION = "/userAuthInvesReturn";
    /** 用户授权自动出借异步回调 */
    public static final String USER_AUTH_INVES_BGRETURN_ACTION = "/userAuthInvesBgreturn";
    
    /** 用户授权自动出借同步回调 */
    public static final String TRANSACTION_URL_ACTION = "/transactionUrl";
    
    /** 用户授权自动出借成功页面*/
    public static final String JUMP_HTML_SUCCESS_PATH = "/user/setting/authorization/result/success";
    /** 用户授权自动出借失败页面*/
    public static final String JUMP_HTML_ERROR_PATH = "/user/setting/authorization/result/failed";
    /** 用户授权自动出借失败页面*/
    public static final String JUMP_HTML_HANDING_PATH = "/user/setting/authorization/result/handing";
	/** 缴费授权同步页面 */
    public static final String PAYMENT_AUTH_ACTION = "/bank/user/paymentAuth/authPage";

}
