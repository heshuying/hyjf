/**
 * Description:解绑卡常量类
 */

package com.hyjf.web.bank.web.user.deletecardpage;

import com.hyjf.web.BaseDefine;

public class DeleteCardPageDefine extends BaseDefine {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/web/deleteCardPage";
	/** @RequestMapping值 */
	public static final String REQUEST_INDEX = "/deleteCard";
	/** @RequestMapping值 */
	public static final String CHECK_MAPPING = "/check";

	// 解卡(页面)
	public static final String DELETE_CARDPAGE ="/deleteCardPage";

	public static final String DELETE_ERROR_PATH= "/bank/user/bankcardNew/closebindcard-error";

	public static final String UNBINDCARD_SUCCESS_PATH= "/bank/user/bankcardNew/closebindcard-success";
	/** 同步回调地址 @RequestMapping值 */
	public static final String RETURL_SYN_ACTION = "/return";
	/** 异步回调地址 @RequestMapping值 */
	public static final String RETURL_ASY_ACTION = "/notifyReturn";

}
