package com.hyjf.admin.manager.hjhplan.planrepay;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class PlanRepayListDefine extends BaseDefine {
	
	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/hjhplan/planrepay";
	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/planrepay/planrepay";
	/** FORM */
	public static final String PLAN_LIST_FORM = "planRepayForm";
	/** 检索面板-检索请求 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	/** exeportExecl */
	public static final String EXPORTEXECL = "exportExcel";

	/** 获取列表 @RequestMapping值 具有 组织机构查看权限*/
	public static final String ENHANCE_EXPORT_ACTION = "enhanceExportAction";

	/** 还款明细请求 @RequestMapping值 */
	public static final String REPAY_INFO = "repayInfoAction"; 
	/** 还款明细列表画面 */
	public static final String REPAY_INFO_LIST_PATH = "/manager/planrepay/repaydetailList";
	/** (债转)还款明细请求 @RequestMapping值 */
	public static final String CREDIT_REPAY_INFO = "creditRepayAction";
	
	/** (债转汇总-hyjf_hjh_debt_credit_tender)还款明细请求 @RequestMapping值 */
    public static final String CREDIT_TENDER_INFO = "creditTenderAction";
	/** 跳转到还款计划的Action @RequestMapping值 */
    public static final String REPAY_PLAN_DETAIL_ACTION = "repayPlanDetailAction";
	/** 还款明细列表画面 */
	public static final String CREDIT_REPAY_INFO_LIST_PATH = "/manager/planrepay/repaycreditdetailList";
	
	/** 债转汇总-hyjf_hjh_debt_credit_tender,列表画面 */
    public static final String CREDIT_TENDER_INFO_LIST_PATH = "/manager/planrepay/tendercreditdetailList";
	/** FORM */
	public static final String REPAY_INFO_FORM = "repayForm"; 
	/** 查看权限 */
	public static final String PERMISSIONS = "planrepay";
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
	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	public static final String ENHANCE_PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.ORGANIZATION_VIEW;

	/** 详细权限 */
    public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;
}
