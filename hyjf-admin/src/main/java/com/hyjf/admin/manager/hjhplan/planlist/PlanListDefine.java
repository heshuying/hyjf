package com.hyjf.admin.manager.hjhplan.planlist;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class PlanListDefine extends BaseDefine {
	
	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/hjhplan/planlist";
	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/hjhplan/planList";
	/** 列表画面扎实 */
	public static final String LIST_INFO_PATH = "manager/hjhplan/planlist/init";
	/** FORM */
	public static final String PLAN_LIST_FORM = "planListForm";
	
	/** 检索面板-检索请求 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	
	/** 迁移到详细画面 @RequestMapping值 */
	public static final String ADD_PLAN_ACTION = "addPlanAction";
	
	/** 开启/关闭 请求 @RequestMapping值 */
	public static final String SWIITCH_ACTION = "switchAction";
	
	/** 显示/隐藏  请求 @RequestMapping值 */
	public static final String DISPLAY_ACTION = "displayAction";
	
	/** 显示/隐藏  请求 @RequestMapping值 */
	public static final String CHECK_STATUS = "checkStatus";
	/**
	 * 显示/隐藏 请求@RequestMapping值
	 */
	public static final String CHECK_DISPLAY_STATUS = "checkDisplayStatus";

	/** 显示/隐藏  请求 @RequestMapping值 */
	public static final String DISPLAY_SHOW_ACTION = "displayShowAction";
	/** 添加计划详情画面的路径 */
	public static final String INFO_PATH = "manager/hjhplan/plancommon";
	/** 添加计划详情画面的路径 */
	public static final String INFO_SHOW_PATH = "manager/hjhplan/plancommonshow";
	
	/** INFO FORM */
	public static final String PLAN_INFO_FORM = "planInfoForm";
	
	/** 判断计划名称是否存在 @RequestMapping */
	public static final String ISDEBTPLANNAMEEXIST_ACTION = "isDebtPlanNameExist";
	
	/** 判断计划编号是否存在 @RequestMapping */
	public static final String ISDEBTPLANNIDEXIST_ACTION = "isDebtPlanNidExist";
	
	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 查看权限 */
	public static final String PERMISSIONS = "planlist";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;
	
	/** exeportExecl */
	public static final String EXPORTEXECL = "exportExcel";
	
	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
	
	/** 运营记录 @RequestMapping值 */
	public static final String OPT_RECORD_ACTION = "optRecordAction";
	
	/** 运营记录-原始标的 */
	public static final String OPT_LOAD_LIST_PATH = "manager/hjhplan/optrecordloan";

	/** 运营记录-债转标的 */
	public static final  String OPT_CREDIT_LIST_PATH = "manager/hjhplan/optrecordcredit";

	/** 运营记录-出借明细 */
	public static final  String OPT_TENDER_LIST_PATH = "manager/hjhplan/optrecordtender";

	/** 运营记录- 承接明细 */
	public static final String OPT_TENDER_DETAIL_LIST_PATH = "manager/hjhplan/optrecordcredittender";
}
