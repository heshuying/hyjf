package com.hyjf.admin.manager.borrow.borrow;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BorrowDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/borrow";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/borrow/borrow";

	/** 详细画面的路径 */
	public static final String INFO_PATH = "manager/borrow/borrow/borrowInfo";
	
	/** 预览画面路径 */
	public static final String PREVIEW_PATH = "manager/borrow/borrow/preview";
	
	/** 删除后 路径 */
	public static final String DELETE_AFTER_PATH = "manager/borrow/borrow/borrow";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";

	/** 导出数据的 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";
	
	/** 预览的 @RequestMapping值 */
	public static final String PREVIEW_ACTION = "previewAction";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

	/** 运营记录 */
	public static final  String OPT_ACTION = "optAction";

	/** FROM */
	public static final String BORROW_FORM = "borrowForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "borrow";

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

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 预览权限 */
	public static final String PERMISSIONS_PREVIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_PREVIEW;
	
	
}
