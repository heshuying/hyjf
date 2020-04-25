
package com.hyjf.app.bank.user.bindcardpage;

import com.hyjf.app.BaseDefine;

public class BindCardPageDefine extends BaseDefine{

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/bindCardPage";
	/** @RequestMapping值 */
	public static final String REQUEST_BINDCARDPAGE = "/bindCardPage";
	
	/** 同步回调地址 @RequestMapping值 */
	public static final String RETURL_SYN_ACTION = "/return";
	/** 异步回调地址 @RequestMapping值 */
	public static final String RETURL_ASY_ACTION = "/notifyReturn";
	
	public static final String BINDCARD_ERROR_PATH="/user/bankCard/bind/result/failed";
	public static final String BINDCARD_SUCCESS_PATH="/user/bankCard/bind/result/success";
	public static final String GET_BIND_CARD_PAGE_URL = "/getBindCardPageUrl";

}
