package com.hyjf.admin.manager.borrow.borrowfirst;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BorrowFirstDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/borrowfirst";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/borrowfirst/borrowfirst";

	/** 详细画面的路径 */
	public static final String INFO_PATH = "manager/borrow/borrow/borrowInfo";

	/** 发标画面 路径 */
	public static final String FIRE_PATH = "manager/borrow/borrowfirst/borrowfire";

	/** 己交保证金详细画面 路径 */
	public static final String BAIL_PATH = "manager/borrow/borrowfirst/borrowbail";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 初审 @RequestMapping值 */
	public static final String FIRST_ACTION = "firstAction";

	/** 发标 @RequestMapping值 */
	public static final String FIRE_ACTION = "fireAction";
	
	/** 保证金 @RequestMapping值 */
	public static final String BAIL_ACTION = "bailAction";

	/** 保证金详细画面 @RequestMapping值 */
	public static final String BAIL_INFO_ACTION = "bailInfoAction";

	/** 发标确定 @RequestMapping值 */
	public static final String FIRE_UPDATE_ACTION = "fireUpdateAction";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

	/** FROM */
	public static final String BORROW_FORM = "borrowFirstForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "borrowfirst";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 已交保证金权限 */
	public static final String PERMISSIONS_BORROW_BAIL = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSIONS_BORROW_BAIL;
	
	/** 已交保证金权限 */
	public static final String PERMISSIONS_DEBT_REGIST = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSIONS_DEBT_REGIST;

	/** 初审 */
	public static final String PERMISSIONS_BORROW_FIRST_AUDIT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSIONS_BORROW_FIRST_AUDIT;

	/** 发标 */
	public static final String PERMISSIONS_BORROW_FIRE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSIONS_BORROW_FIRE;

}
