
package com.hyjf.wechat.controller.user.bindcard;

import com.hyjf.wechat.base.BaseDefine;

public class BindCardDefine extends BaseDefine{

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/wx/bindCard";
	/** @RequestMapping值 */
	public static final String REQUEST_BINDCARD = "/bindCard";
	/** @RequestMapping值 */
	public static final String CHECK_MAPPING = "/check";
	/** @RequestMapping值 */
	public static final String INDEX_MAPPING = "/index";
	/** @RequestMapping值 */
	public static final String GET_BIND_CARD_INFO = "/getBindCardInfo";

	/** 绑卡错误页面 */
	public static final String JSP_BINDCARD = "/bank/user/bindcard/bindcard";
	/** 绑卡错误页面 */
	public static final String JSP_BINDCARD_FALSE = "/bank/user/bindcard/bindcard_false";
	
	/** 江西银行 发送短信码 */
    public static final String SEND_PLUS_CODE_ACTION = "/sendPlusCode";
    /** 江西银行绑卡增强 */
    /** @RequestMapping值 */
    public static final String BIND_CARD_PLUS = "/bindCardPlus";
	
	/** 同步回调地址 @RequestMapping值 */
	public static final String RETURN_MAPPING = "/return";
	/** 异步回调地址 @RequestMapping值 */
	public static final String NOTIFY_RETURN_MAPPING = "/notifyReturn";
}
