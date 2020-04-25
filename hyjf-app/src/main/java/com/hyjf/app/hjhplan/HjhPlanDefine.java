package com.hyjf.app.hjhplan;

import com.hyjf.app.BaseDefine;

public class HjhPlanDefine extends BaseDefine {
	
	/** 统一包访问路径 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/plan";
	
	/** 汇计划项目详情 @RequestMapping值 */
	public static final String HJH_PLAN_DETAIL_ACTION = "/{planId}";
	
	/** 汇计划加入记录 @RequestMapping值 */
	public static final String HJH_PLAN_ACCEDE_ACTION = "/{planId}/investRecord";
	
	/** 汇计划债权(标的组成) @Path值 */
	public static final String HJH_PLAN_BORROW_ACTION = "/{planId}/borrowComposition";

}
