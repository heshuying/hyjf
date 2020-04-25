package com.hyjf.web.hjhplan;

import com.hyjf.web.BaseDefine;

public class HjhPlanDefine extends BaseDefine {

	/** 指定类型的项目 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/hjhplan";
	
	/** 初始化指定类型的项目列表 @RequestMapping值 */
	public static final String INIT_PLAN_LIST_ACTION = "/initPlanList";
	
    /**  汇添金计划列表 @Path值 */
    public static final String PLAN_LIST_PTAH = "hjhplan/hjhplanList";
    
    /** 指定类型的项目项目列表 @RequestMapping值 */
    public static final String PLAN_LIST_ACTION = "/getPlanList";


}
