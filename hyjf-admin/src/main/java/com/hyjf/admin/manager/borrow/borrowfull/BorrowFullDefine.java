package com.hyjf.admin.manager.borrow.borrowfull;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BorrowFullDefine extends BaseDefine {

	/** CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/borrowfull";

	/** 复审记录 路径 */
	public static final String LIST_PATH = "manager/borrow/borrowfull/borrowfull";

	/** 复审 路径 */
	public static final String REVERIFY_PATH = "manager/borrow/borrowfull/borrowfull_reverify";

	/** 初始化 @RequestMapping值 */
	public static final String INIT = "init";

	/** 检索 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 复审详细画面 @RequestMapping值 */
	public static final String FULL_INFO_ACTION = "fullInfoAction";

	/** 复审详细画面 确定按钮 @RequestMapping值 */
	public static final String FULL_ACTION = "fullAction";

	/** 重新放款 按钮 @RequestMapping值 */
	public static final String REPEAT_ACTION = "repeatAction";

	/** 流标 按钮 @RequestMapping值 */
	public static final String OVER_ACTION = "overAction";

	/** FROM */
	public static final String BORROW_FORM = "borrowFullForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "borrowfull";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 复审 */
	public static final String PERMISSIONS_BORROW_FULL = PERMISSIONS + StringPool.COLON + ShiroConstants.BORROW_FULL;

	/** 流标 */
	public static final String PERMISSIONS_BORROW_OVER = PERMISSIONS + StringPool.COLON + ShiroConstants.BORROW_OVER;

}
