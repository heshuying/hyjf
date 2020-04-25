package com.hyjf.app.bank.user.deletecardpage;

import com.hyjf.app.BaseDefine;

/**
 * App解绑银行卡常量类
 * 
 * @author liuyang
 *
 */
public class AppDeleteCardPageDefine extends BaseDefine {
	/** app解绑银行卡 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/app/deleteCardPage";
	/** 跳转成功页面 */
	public static final String JUMP_HTML_SUCCESS_PATH = "/user/bankCard/unbind/result/success";
	/** 跳转失败页面 */
	public static final String JUMP_HTML_ERROR_PATH = "/user/bankCard/unbind/result/failed";
	/** 跳转中间页面 */
	public static final String JUMP_HTML_HANDLING_PATH = "/user/bankCard/unbind/result/handing";

	//合规接口改造 start
	/** 删除银行卡请求 @RequestMapping值 (页面调用) */
	public static final String REQUEST_INDEXPAGE = "/deleteCardPage";

	public static final String REQUEST_DELETE_CARD = "/deleteCard";
	/** 同步回调地址 @RequestMapping值 */
	public static final String RETURL_SYN_ACTION = "/return";
	/** 异步回调地址 @RequestMapping值 */
	public static final String RETURL_ASY_ACTION = "/notifyReturn";

	public static final String GET_REQUEST_DELETE_CARD_REQUEST = REQUEST_HOME + REQUEST_MAPPING + REQUEST_DELETE_CARD;

}
