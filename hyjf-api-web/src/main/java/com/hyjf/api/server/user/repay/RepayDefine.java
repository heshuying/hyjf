package com.hyjf.api.server.user.repay;

import com.hyjf.base.bean.BaseDefine;

/**
 * 还款常量类
 * @author cwyang
 *
 */
public class RepayDefine extends BaseDefine{

	
	public static final String REQUEST_MAPPING = "/server/user/repay";
	/**还款冻结*/
	public static final String REPAY_ACTION = "/repay";
	/**查看还款批次处理结果*/
	public static final String REPAY_RESULT_ACTION = "/getrepayresult";
	/**获取标的还款计划信息*/
	public static final String REPAY_INFO_ACTION = "/getrepayinfo";
	
	
}
