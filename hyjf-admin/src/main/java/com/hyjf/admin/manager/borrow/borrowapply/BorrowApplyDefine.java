package com.hyjf.admin.manager.borrow.borrowapply;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BorrowApplyDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/borrowapply";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/borrowapply/borrowapply";

	/** 详细画面 路径 */
	public static final String INFO_PATH = "manager/borrow/borrowapply/borrowapply_info";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 检索数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** FROM */
	public static final String BORROW_FORM = "borrowApplyForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "borrowapply";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 审核权限 */
	public static final String PERMISSION_AUDIT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_AUDIT;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 详细权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

}
