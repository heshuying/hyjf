package com.hyjf.admin.exception.freezexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class FreezExceptionDefine extends BaseDefine {

	/** CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/freezexception";

	/** 解冻异常画面 路径 */
	public static final String LIST_PATH = "exception/freezexception/freezexception";

	/** 解冻明细画面 路径 */
	public static final String INFO_PATH = "exception/freezexception/freezexception_info";

	/** 初始化 @RequestMapping值 */
	public static final String INIT = "init";

	/** 检索 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 解冻 @RequestMapping值 */
	public static final String FREEZE_INFO_ACTION = "freezeInfoAction";

	/** 解冻 按钮 @RequestMapping值 */
	public static final String FREEZE_ACTION = "freezeAction";

	/** FROM */
	public static final String FORM = "form";

	/** 查看权限 */
	public static final String PERMISSIONS = "freezexception";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 资金解冻权限 */
	public static final String PERMISSIONS_FREEZE = PERMISSIONS + StringPool.COLON + "FREEZE";
}
