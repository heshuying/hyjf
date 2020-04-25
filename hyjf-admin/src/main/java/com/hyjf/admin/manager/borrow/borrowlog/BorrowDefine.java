package com.hyjf.admin.manager.borrow.borrowlog;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BorrowDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/borrowlog";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/borrowlog/borrowlog";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	
	/** 导出数据的 @RequestMapping值 */
    public static final String EXPORT_ACTION = "exportAction";

	

	/** FROM */
	public static final String BORROW_LOG_FORM = "borrowLogForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "borrowlog";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
}
