package com.hyjf.web.user.wechatbindcard;

import com.hyjf.web.BaseDefine;

public class UserWeChatBindCardDefine extends BaseDefine{

	public static final String REQUEST_MAPPING = "/weChatBindCard";
	
	public static final String REQUEST_BINDCARD_MAPPING = "/bindCard";
	/** 绑卡错误页面 */
    public static final String JSP_BINDCARD_FALSE = "/user/bindcard/bindcard_false";
    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_SEND = "/chinapnr/chinapnr_send";
	
	/** @RequestMapping值 */
    public static final String RETURN_MAPPING = "/return";
}
