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
package com.hyjf.web.plan.borrow;

import com.hyjf.web.BaseDefine;

public class PlanBorrowDefine extends BaseDefine {

	/** 指定类型的项目 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "plan";
	
	/** 初始化指定类型的项目列表 @RequestMapping值 */
	public static final String INIT_PROJECT_LIST_ACTION = "/initProjectList";
	
	/** 指定类型的项目项目列表 @RequestMapping值 */
	public static final String PROJECT_LIST_ACTION = "/getProjectList";

	/** 项目详情 @RequestMapping值 */
	public static final String PROJECT_DETAIL_ACTION = "/getProjectDetail";
	
	/** 预约项目详情 @RequestMapping值 */
	public static final String APPOINTMENT_DETAIL_ACTION = "/appointmentDetail";
	
	/** 项目预览 @RequestMapping值 */
	public static final String PROJECT_PREVIEW_ACTION = "/getProjectPreview";
	
	/** 出借记录 @RequestMapping值 */
	public static final String PROJECT_INVEST_ACTION = "/getProjectInvest";

	/** 出借记录 @RequestMapping值 */
	public static final String PROJECT_CONSUME_ACTION = "/getProjectConsume";
	
	/** 项目详情 @RequestMapping值 */
	public static final String PROJECT_STATUS_ACTION = "/getProjectStatus";

	/** 汇直投项目详情 @Path值 */
	public static final String HZT_PROJECT_DETAIL_PTAH = "project/projectDetail";
	
	/** 汇直投项目详情预览 @Path值 */
	public static final String HZT_PROJECT_PREVIEW_PTAH = "project/projectview";
	
	/**  汇直投项目列表 @Path值 */
	public static final String PROJECT_LIST_PTAH = "project/projectList";
	
	/** 项目详情 @Path值 */
	public static final String ERROR_PTAH = "error";

	/** 类名 */
	public static final String THIS_CLASS = PlanBorrowController.class.getName();

}
