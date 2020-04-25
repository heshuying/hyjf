package com.hyjf.wechat.controller.bindCard;

import com.hyjf.wechat.base.BaseDefine;

/**
 * App绑卡常量类
 * 
 * @author liuyang
 *
 */
public class WxBindCardDefine extends BaseDefine {

	/** 绑卡页面 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/bindCard";
	/** 绑卡页面 @RequestMapping 值 */
	public static final String REQUEST_INIT = "/init";
	/** 绑卡页面发送验证码 @RequestMapping 值 */
	public static final String REQUEST_SEND_SMS_CODE = "/user/bankCard/bind/smscode";
	/** @RequestMapping值 */
	public static final String REQUEST_BINDCARD = "/bindCard";
	/** 同步回调地址 @RequestMapping值 */
	public static final String RETURN_MAPPING = "/return";
	/** 异步回调地址 @RequestMapping值 */
	public static final String NOTIFY_RETURN_MAPPING = "/notifyReturn";
	/** 绑卡画面路径 @RequestMapping */
	public static final String BINGCARD_INIT_PATH = "/bank/user/bindcard/bindcard";
	/** 绑卡成功画面 @RequestMapping */
	public static final String BINDCARD_SUCCESS_PATH = "/bindcard/bindcard_success";
	/** 绑卡错误画面 @RequestMapping */
	public static final String BINDCARD_ERROR_PATH = "/bindcard/bindcard_fail";
	/** 绑卡处理中画面 @RequestMapping */
	public static final String BINDCARD_PROCESS_PATH = "/bindcard/bindcard_process";
	/** 绑卡画面路径 @RequestMapping */
	public static final String BINDCARD_ACTION = "/user/bankCard/bind";
	/** 绑卡错误页面 */
	public static final String JUMP_HTML_ERROR_PATH = "/user/bankCard/bind/result/failed";
	/** 绑卡成功页面 */
	public static final String JUMP_HTML_SUCCESS_PATH = "/user/bankCard/bind/result/success";
	/** 绑卡处理中页面 */
	public static final String JUMP_HTML_HANDLING_PATH = "/user/bankCard/bind/result/handing";

    /**本地跳转的页面*/
    public static final String JUMP_HTML = "/user/result/jump_html";
}
