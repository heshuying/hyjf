/**
 * Description:获取指定的项目类型的项目常量定义
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.api.web.plan;

import com.hyjf.base.bean.BaseDefine;

public class PlanDefine extends BaseDefine {

	/** 指定类型的项目 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/plan";
	
	/** 初始化指定类型的项目列表 @RequestMapping值 */
	public static final String INIT_PLAN_LIST_ACTION = "/initPlanList";
	
	/** 指定类型的项目项目列表 @RequestMapping值 */
	public static final String PLAN_LIST_ACTION = "/getPlanList";

	/** 项目详情 @RequestMapping值 */
	public static final String PLAN_DETAIL_ACTION = "/getPlanDetail";
	
	/** 预约项目详情 @RequestMapping值 */
	public static final String APPOINTMENT_DETAIL_ACTION = "/appointmentDetail";
	
	/** 项目预览 @RequestMapping值 */
	public static final String PLAN_PREVIEW_ACTION = "/getPlanPreview";
	
	/** 计划加入记录 @RequestMapping值 */
	public static final String PLAN_ACCEDE_ACTION = "/getPlanAccedeList";

	/** 计划标的列表 @Path值 */
	public static final String PLAN_BORROW_ACTION = "/getPlanBorrowList";
	/** 获取出借信息 @RequestMapping值 */
	public static final String INVEST_INFO_ACTION="/investInfo";
	/** 获取实时收益 @RequestMapping值 */
	public static final String INVEST_EARNINGS_ACTION="/getInvestEarnings";
	/** @RequestMapping值 */
	public static final String PLAN_CHECK_ACTION = "/planCheck";
	/** 授权@RequestMapping值 */
	public static final String APPOINTMENT_ACTION = "/appointment";
	/** 授权校验@RequestMapping值 */
	public static final String APPOINTMENT_CHECK_ACTION = "/appointmentCheck";
	/** @RequestMapping值 */
	public static final String PLAN_INVEST_ACTION = "/planInvest";
	/** @RequestMapping值 */
	public static final String XIEYI_ACTION = "/xieyi";
	/** @RequestMapping值 */
	public static final String XY_PATH = "plan/xieyi";
	/** 计划详情 @Path值 */
	public static final String PLAN_DETAIL_PTAH = "plan/planDetail";
	
	/** 汇直投项目详情预览 @Path值 */
	public static final String PLAN_PREVIEW_PTAH = "plan/planview";
	
	/**  汇添金计划列表 @Path值 */
	public static final String PLAN_LIST_PTAH = "plan/planList";

	/** 项目详情 @Path值 */
	public static final String ERROR_PTAH = "error";
	
	/** 授权回调地址 */
	public static final String RETURL_SYN_ACTION = "/appointmentRetUrl";
	
	/** 类名 */
	public static final String THIS_CLASS = PlanController.class.getName();

}
