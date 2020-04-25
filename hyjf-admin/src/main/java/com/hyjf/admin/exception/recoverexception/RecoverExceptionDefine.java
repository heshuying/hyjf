package com.hyjf.admin.exception.recoverexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class RecoverExceptionDefine extends BaseDefine {

	/** CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/recoverexception";

	/** 放款异常画面 路径 */
	public static final String LIST_PATH = "exception/recoverexception/recoverexception";

	/** 放款明细画面 路径 */
	public static final String INFO_PATH = "exception/recoverexception/recoverexception_info";

	/** 初始化 @RequestMapping值 */
	public static final String INIT = "init";

	/** 检索 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 检索 @RequestMapping值 */
	public static final String SEARCH_INFO_ACTION = "searchInfoAction";

	/** 放款明细 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 重新放款 按钮 @RequestMapping值 */
	public static final String REPEAT_ACTION = "repeatAction";

	/** FROM */
	public static final String FORM = "form";

	/** 查看权限 */
	public static final String PERMISSIONS = "recoverexception";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 放款异常修复权限 */
	public static final String PERMISSIONS_RECOVER_EXCEPTION = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_RECOVER_EXCEPTION;
}
