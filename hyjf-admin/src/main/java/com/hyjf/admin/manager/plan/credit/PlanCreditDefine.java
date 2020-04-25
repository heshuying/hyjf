package com.hyjf.admin.manager.plan.credit;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class PlanCreditDefine extends BaseDefine {
	
	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String CONTROLLER_NAME = PlanCreditController.class.getName();

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/plancredit";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT_ACTION = "init";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 导出 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";
	
	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/plancredit/plancredit";

	/** FROM */
	public static final String PLAN_CREDIT_FORM = "planCreditForm";
	
	/** 跳转到还款计划的Action @RequestMapping值 */
	public static final String CREDIT_DETAIL_ACITON = "redirect:/manager/borrow/plancredit/credittender/init";
	
	/** FROM */
	public static final String PLAN_CREDIT_TENDER_FORM = "creditTenderForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "planCredit";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 详细 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

	/** 导出 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
