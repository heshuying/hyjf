package com.hyjf.server.module.wkcd.user.openAccount;

import com.hyjf.server.BaseDefine;

public class WkcdOpenAccountDefine extends BaseDefine {

	/** 类名 */
	public static final String THIS_CLASS = WkcdOpenAccountController.class.getName();
	
	/** Controller @RequstMapping */
	public static final String REQUEST_MAPPING = "/user/openAccount";

	/** 用户开户 @RequestMapping值 */
	public static final String OPEN_ACCOUNT_ACTION = "openAccountAction";
	/** 开户 @RequestMapping值 */
	public static final String OPENACCOUNT_MAPPING = "/open";
	/** 开户汇付返回同步回调 @RequestMapping值 */
	public static final String RETURL_SYN_MAPPING = "/return";
	/** 开户汇付返回异步回调 @RequestMapping值 */
	public static final String RETURN_ASY_MAPPING = "/callback";
	/** 用户是否开户 @RequestMapping值 */
	public static final String IF_OPENED_ACCOUNT_ACTION = "ifOpenedAction";
	
	/** JSP 汇付天下跳转画面 */
    public static final String JSP_CHINAPNR_SEND = "/chinapnr/chinapnr_send";
    /** JSP 回调画面 */
	public static final String OPEN_SUCCESS_PATH = "/open/open_success";
	/** JSP 回调画面 */
	public static final String OPEN_ERROR_PATH = "/open/open_error";

    /** 开户返回页面 */
    public static final String OPENACCOUNT_JSP = "/server/serverReturn";
}
