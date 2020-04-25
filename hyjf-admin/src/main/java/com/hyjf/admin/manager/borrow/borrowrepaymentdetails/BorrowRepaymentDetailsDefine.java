package com.hyjf.admin.manager.borrow.borrowrepaymentdetails;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BorrowRepaymentDetailsDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/borrowrepaymentdetails";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/borrowrepaymentdetails/borrowrepaymentdetails";

	/** 初始化 */
	public static final String INIT = "init";

	/** 跳转到还款明细列表 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 导出Excel表格Action */
	public static final String EXPORT_ACTION = "exportAction";

	/** FROM */
	public static final String REPAYMENTINFO_FORM = "borrowRepaymentDetailsForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "repaymentdetails";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
