package com.hyjf.admin.exception.borrowregistexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BorrowRegistExceptionDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/borrowregistexception";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "exception/borrowregistexception/borrowregistexception";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT_ACTION = "init";
	
	/** 列表画面查询 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	
	/** 列表画面查询 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

	/** 查看删除列表画面 @RequestMapping值 */
	public static final String BORROW_REGIST_EXCEPTION= "borrowregistexception";

	/** FROM */
	public static final String BORROW_REGIST_EXCEPTION_FORM = "borrowregistexceptionForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "debtregistexception";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 标的备案异常  */
	public static final String PERMISSIONS_DEBTREGISTEXCEP = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSIONS_DEBTREGISTEXCEP;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
