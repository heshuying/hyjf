package com.hyjf.wechat.controller.user.synbalance;

import com.hyjf.wechat.base.BaseDefine;

public class WxSynBalanceDefine extends BaseDefine  {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/wx/bank/user/synbalance";

	/** 同步余额    */
	public static final String INIT = "/init";
		
	/** 当前controller名称 */
	public static final String THIS_CLASS = WxSynBalanceController.class.getName();
}
