package com.hyjf.admin.exception.tendercancelexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class TenderCancelExceptionDefine extends BaseDefine {

	/** CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/tendercancelexception";

	/** 解冻异常画面 路径 */
	public static final String LIST_PATH = "exception/tendercancelexception/tendercancelexception";

	/** 初始化 @RequestMapping值 */
	public static final String INIT = "init";

	/** 检索 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 解冻 按钮 @RequestMapping值 */
	public static final String BIDCANEL_ACTION = "bidCancelAction";
	
	/** 查询标的状态 按钮 @RequestMapping值 */
	public static final String QUERY_BORRORSTATUS_ACTION = "queryBorrowStatusAction";

	/** FROM */
	public static final String FORM = "form";

	/** 查看权限 */
	public static final String PERMISSIONS = "bidcancelexception";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 资金解冻权限 */
	public static final String PERMISSIONS_BIDCANCEL = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSIONS_BIDCANCEL;
}
