package com.hyjf.admin.manager.user.userauth;

public class UserPayDefine {
	// 缴费的链接
	public static final String USERPAYAUTH_LIST_ACTION = "userpayauthlist";
	public static final String USER_PAY_AUTH_LIST_PATH = "manager/users/userauthlist/userpayauthlist";
	public static final String USER_PAY_AUTH_QUERY = "userpayauthquery";
	public static final String USER_PAY_AUTH_DISMISS="userpayauthdis";
	public static final String USER_PAY_CANCEL_ACTION="paycancel";
	
	public static final String USERPAY_LIST_FORM = "userpayauthListForm";
	// 付款的链接
	public static final String USERREPAYAUTH_LIST_ACTION = "userrepayauthlist";
	public static final String USER_REPAY_AUTH_LIST_PATH = "manager/users/userauthlist/userrepayauthlist";
	public static final String USER_REPAY_AUTH_QUERY = "userrepayauthquery";
	
	public static final String USER_REPAY_CANCEL_ACTION="repaycancel";
	
	
	/** 获取会员授权列表 @RequestMapping值 */
	public static final String EXPORT_PAYAUTH_ACTION = "exportpayauth";
	public static final String EXPORT_REPAYAUTH_ACTION = "exportrepayauth";
}
