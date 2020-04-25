package com.hyjf.app.bank.user.repayauthpage;

import com.hyjf.app.BaseDefine;

public class RepayAuthPageDefine extends BaseDefine {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/repayAuth";
	/** 用户自动出借增强授权 */
    public static final String USER_REPAY_AUTH_ACTION = "/userRepayAuth";

    /** 用户授权自动出借同步回调 */
    public static final String USER_REPAY_AUTH_RETURN_ACTION = "/userRepayAuthReturn";
    /** 用户授权自动出借异步回调 */
    public static final String USER_REPAY_AUTH_BGRETURN_ACTION = "/userRepayAuthBgreturn";

    //合规接口改造 start
    /** 缴费授权验证失败*/
    public static final String JUMP_HTML_FAILED_PATH = "/user/setting/repayauth/result/failed";
    /** 缴费授权处理中*/
    public static final String JUMP_HTML_HANDLING_PATH = "/user/setting/repayauth/result/handling";
    /** 缴费授权成功*/
    public static final String JUMP_HTML_SUCCESS_PATH = "/user/setting/repayauth/result/success";
    

}
