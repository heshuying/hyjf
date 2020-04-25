package com.hyjf.app.bank.user.deletecard;

import com.hyjf.app.BaseDefine;

/**
 * App解绑银行卡常量类
 * 
 * @author liuyang
 *
 */
public class AppDeleteCardDefine extends BaseDefine {
	/** app解绑银行卡 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/app/deleteCard";
	/** 删除银行卡请求 @RequestMapping值 */
	public static final String REQUEST_INDEX = "/deleteCard";
	/** 跳转成功页面 */
	public static final String JUMP_HTML_SUCCESS_PATH = "/user/bankCard/unbind/result/success";
	/** 跳转失败页面 */
	public static final String JUMP_HTML_ERROR_PATH = "/user/bankCard/unbind/result/failed";
	/** 跳转中间页面 */
	public static final String JUMP_HTML_HANDLING_PATH = "/user/bankCard/unbind/result/handling";
}
