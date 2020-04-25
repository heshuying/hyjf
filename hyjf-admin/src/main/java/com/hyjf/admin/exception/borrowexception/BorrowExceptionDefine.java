package com.hyjf.admin.exception.borrowexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BorrowExceptionDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/borrowexception";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "exception/borrowexception/borrowexception";

	/** 删除日志查看列表的路径 */
	public static final String DELETE_LIST_PATH = "exception/borrowexception/borrowexceptiondelete";

	/** 删除后 路径 */
	public static final String DELETE_AFTER_PATH = "exception/borrowexception/borrowexception";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 查看删除列表画面 @RequestMapping值 */
	public static final String DELETEINIT = "borrowdeleteinit";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";

	/** 标的撤销 @RequestMapping值 */
	public static final String REVOKE_ACTION = "revokeAction";

	/** 标的异常导出 @RequestMapping */
	public static final String EXPORT_ACTION = "exportAction";
	/** 删除记录导出 @RequestMapping */
	public static final String EXPORT_BORROWDEL_EXCEL_ACTION = "exportBorrowDelExcelAction";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

	/** FROM */
	public static final String BORROW_FORM = "borrowexceptionForm";
	/** FROM */
	public static final String BORROW_DELETE_FORM = "borrowexceptiondeleteForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "borrowexceptiondel";
	/** 查看权限 */
	public static final String PERMISSIONSLOG = "borrowexceptionlog";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 标的撤销权限 */
	public static final String PERMISSIONS_REVOKE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSIONS_REVOKE;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 查看权限 */
	public static final String PERMISSIONSLOG_VIEW = PERMISSIONSLOG + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONSLOG_SEARCH = PERMISSIONSLOG + StringPool.COLON
			+ ShiroConstants.PERMISSION_SEARCH;

	/** 修改权限 */
	public static final String PERMISSIONSLOG_MODIFY = PERMISSIONSLOG + StringPool.COLON
			+ ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONSLOG_DELETE = PERMISSIONSLOG + StringPool.COLON
			+ ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONSLOG_ADD = PERMISSIONSLOG + StringPool.COLON + ShiroConstants.PERMISSION_ADD;
}
