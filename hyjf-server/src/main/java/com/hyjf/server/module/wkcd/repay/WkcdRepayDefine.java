package com.hyjf.server.module.wkcd.repay;

import com.hyjf.server.BaseDefine;

public class WkcdRepayDefine extends BaseDefine {

	/** 类名 */
	public static final String THIS_CLASS = WkcdRepayController.class.getName();
	
	/** Controller @RequstMapping */
	public static final String REQUEST_MAPPING = "/user/repay";

	/** 用户还款 @RequestMapping值 */
	public static final String USER_REPAY_ACTION = "repayAction";


}
