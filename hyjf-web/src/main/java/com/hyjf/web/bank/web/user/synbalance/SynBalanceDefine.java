package com.hyjf.web.bank.web.user.synbalance;

import com.hyjf.web.BaseDefine;

public class SynBalanceDefine extends BaseDefine  {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/synbalance";

	/** 同步余额    */
	public static final String INIT = "/init";
		
	/** 当前controller名称 */
	public static final String THIS_CLASS = SynBalanceController.class.getName();
}
