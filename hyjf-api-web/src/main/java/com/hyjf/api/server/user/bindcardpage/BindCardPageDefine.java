package com.hyjf.api.server.user.bindcardpage;

public class BindCardPageDefine  {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/server/user/bindcardpage";
	/** 绑卡接口地址 */
	public static final String BIND_CARD_PAGE = "/bind";
	
	/** 同步回调  */
    public static final String RETURL_SYN_ACTION = "/bindCardReturn";
    /** 异步回调  */
    public static final String RETURL_ASY_ACTION = "/bindCardBgreturn";

}



