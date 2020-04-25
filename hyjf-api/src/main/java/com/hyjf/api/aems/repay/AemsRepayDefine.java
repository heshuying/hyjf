package com.hyjf.api.aems.repay;

import com.hyjf.base.bean.BaseDefine;

/**
 * 还款常量类
 * @author cwyang
 *
 */
public class AemsRepayDefine extends BaseDefine{

	
	public static final String REQUEST_MAPPING = "/aems/repay";

	/**获取标的还款计划信息*/
	public static final String REPAY_INFO_ACTION = "/getrepayinfo";

	/**获取标的还款计划信息详情*/
	public static final String REPAY_INFO_LIST_ACTION = "/getrepayinfolist";
	
	
}
