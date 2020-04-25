package com.hyjf.web.bank.wechat.user.synbalance;

import com.hyjf.web.BaseDefine;

public class WeChatSynBalanceDefine extends BaseDefine  {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/wechat/user/synbalance";

	/** 同步余额    */
	public static final String INIT = "/init";
		
	/** 当前controller名称 */
	public static final String THIS_CLASS = WeChatSynBalanceController.class.getName();
}
