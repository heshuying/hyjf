
package com.hyjf.wechat.controller.user.bindcardpage;

import com.hyjf.wechat.base.BaseDefine;

public class BindCardPageDefine extends BaseDefine{

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/wx/bindCardPage";
	/** @RequestMapping值 */
	public static final String REQUEST_BINDCARDPAGE = "/bindCardPage";
	
	/** 同步回调地址 @RequestMapping值 */
	public static final String RETURL_SYN_ACTION = "/return";
	/** 异步回调地址 @RequestMapping值 */
	public static final String RETURL_ASY_ACTION = "/notifyReturn";
	
	public static final String BINDCARD_ERROR_PATH="/user/bankCard/bind/result/failed";
	public static final String BINDCARD_SUCCESS_PATH="/user/bankCard/bind/result/success";

}
