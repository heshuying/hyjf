
package com.hyjf.web.bank.web.user.bindcardpage;

import com.hyjf.web.BaseDefine;

public class BindCardPageDefine extends BaseDefine{

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/web/bindCardPage";
	/** @RequestMapping值 */
	public static final String REQUEST_BINDCARDPAGE = "/bindCardPage";
	
	/** 同步回调地址 @RequestMapping值 */
	public static final String RETURL_SYN_ACTION = "/return";
	/** 异步回调地址 @RequestMapping值 */
	public static final String RETURL_ASY_ACTION = "/notifyReturn";
	
	public static final String BINDCARD_ERROR_PATH="/bank/user/bankcardNew/bindcard-error";
	public static final String BINDCARD_SUCCESS_PATH="/bank/user/bankcardNew/bindcard-success";

}
