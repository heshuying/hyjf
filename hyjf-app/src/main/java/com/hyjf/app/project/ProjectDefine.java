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
package com.hyjf.app.project;

import com.hyjf.app.BaseDefine;

public class ProjectDefine extends BaseDefine {

	/** 指定类型的项目 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/project";

	/** 指定类型的项目项目列表 @RequestMapping值 */
	public static final String PROJECT_LIST_ACTION = "/getProjectList";

	/** 项目详情 @RequestMapping值 */
	public static final String PROJECT_DETAIL_ACTION = "/getProjectDetail";

	/** 项目信息 @RequestMapping值 */
	public static final String PROJECT_INFO_ACTION = "/getProjectInfo";

	/** 风控信息 @RequestMapping值 */
	public static final String RISK_CONTROL_ACTION = "/getRiskControl";

	/** 处置预案 @RequestMapping值 */
	public static final String DISPOSAL_PLAN_ACTION = "/getDisposalPlan";

	/** 相关文件 */
	public static final String FILES_ACTION = "/getFiles";

	/** 出借记录 @RequestMapping值 */
	public static final String PROJECT_INVEST_ACTION = "/getProjectInvest";

	/** 出借记录 @RequestMapping值 */
	public static final String PROJECT_CONSUME_ACTION = "/getProjectConsume";

	/** 项目详情 @Path值 */
	public static final String PROJECT_DETAIL_PTAH = "projectdetail/project_detail";
	
	/** 项目详情 @Path值 */
	public static final String ERROR_PTAH = "error";
	
	/** 用户登录url @Url值 */
	public static final String USER_LOGIN_URL = "://jumpLogin";
	
	/** 用户开户url @Url值 */
	public static final String USER_OPEN_URL = "://jumpH5";
	
	/** 用户出借url @Url值 */
	public static final String USER_INVEST_URL = "://jumpPayForProject";
	
	/** 项目列表每页显示条数不大于此数 */
	public static final int PROJEC_TOTAL = 20;

	/** 类名 */
	public static final String THIS_CLASS = ProjectController.class.getName();

	/** 项目信息 @RequestMapping值 */
	public static final String PROJECT_DETAIL_CONSTANT_TABNAME_PROJECTINFO = "项目信息";

	/** 风控信息 @RequestMapping值 */
	public static final String PROJECT_DETAIL_CONSTANT_TABNAME_RISKCONTROLL = "风控信息";

	/** 处置预案 @RequestMapping值 */
	public static final String PROJECT_DETAIL_CONSTANT_TABNAME_DISPOSALPLAN = "处置预案";

	/** 相关文件 @RequestMapping值 */
	public static final String PROJECT_DETAIL_CONSTANT_TABNAME_FILES = "相关文件";

	/** 出借记录@RequestMapping值 */
	public static final String PROJECT_DETAIL_CONSTANT_TABNAME_INVEST = "出借记录";

	/** 常见问题@RequestMapping值 */
	public static final String PROJECT_DETAIL_CONSTANT_TABNAME_QUESTION = "常见问题";

}
