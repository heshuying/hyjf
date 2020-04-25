package com.hyjf.admin.exception.debtborrowexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class DebtBorrowExceptionDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/debtborrowexception";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "exception/debtborrowexception/debtborrowexception";

	/** 删除日志查看列表的路径 */
	public static final String DELETE_LIST_PATH = "exception/debtborrowexception/debtborrowexceptiondelete";

	/** 删除后 路径 */
	public static final String DELETE_AFTER_PATH = "exception/debtborrowexception/debtborrowexception";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	
	/** 查看删除列表画面 @RequestMapping值 */
	public static final String DELETEINIT = "borrowdeleteinit";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

	/** FROM */
	public static final String BORROW_FORM = "borrowexceptionForm";
	/** FROM */
	public static final String BORROW_DELETE_FORM = "borrowexceptiondeleteForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "debtexceptiondel";
	/** 查看权限 */
    public static final String PERMISSIONSLOG = "debtexceptionlog";

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
	
	/** 查看权限 */
    public static final String PERMISSIONSLOG_VIEW = PERMISSIONSLOG + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 检索权限 */
    public static final String PERMISSIONSLOG_SEARCH = PERMISSIONSLOG + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /** 修改权限 */
    public static final String PERMISSIONSLOG_MODIFY = PERMISSIONSLOG + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

    /** 删除权限 */
    public static final String PERMISSIONSLOG_DELETE = PERMISSIONSLOG + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

    /** 添加权限 */
    public static final String PERMISSIONSLOG_ADD = PERMISSIONSLOG + StringPool.COLON + ShiroConstants.PERMISSION_ADD;
}
