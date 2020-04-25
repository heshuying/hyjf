package com.hyjf.admin.manager.borrow.borrowrecover;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BorrowRecoverDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/borrowrecover";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/borrowrecover/borrowrecover";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 检索数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 导出数据 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

	/** 导出数据 @RequestMapping值 具有 组织机构查看权限*/
	public static final String ENHANCE_EXPORT_ACTION = "enhanceExportAction";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

	/** FROM */
	public static final String BORROW_FORM = "borrowRecoverForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "borrowrecover";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	public static final String ENHANCE_PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.ORGANIZATION_VIEW;

}
