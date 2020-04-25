package com.hyjf.admin.manager.borrow.borrowregist;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BorrowRegistDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/borrowregist";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/borrowregist/borrowregist";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 标的备案@RequestMapping值 */
	public static final String DEBT_REGIST_ACTION = "debtRegistAction";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

	/** FROM */
	public static final String BORROW_REGIST_FORM = "borrowRegistForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "borrowRegist";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
	
	/** 已交保证金权限 */
	public static final String PERMISSIONS_DEBT_REGIST = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSIONS_DEBT_REGIST;



}
