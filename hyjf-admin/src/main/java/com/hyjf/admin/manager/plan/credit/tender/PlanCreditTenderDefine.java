package com.hyjf.admin.manager.plan.credit.tender;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class PlanCreditTenderDefine extends BaseDefine {
	
	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String CONTROLLER_NAME = PlanCreditTenderController.class.getName();

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/plancredit/credittender";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/plancredit/credittender/credittender";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 导出 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

	/** FROM */
	public static final String FORM = "creditTenderForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "planCreditTender";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
