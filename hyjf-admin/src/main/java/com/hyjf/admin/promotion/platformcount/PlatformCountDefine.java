package com.hyjf.admin.promotion.platformcount;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class PlatformCountDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/promotion/platformcount";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "promotion/platformcount/platformcount";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 导出数据的 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

	/** FROM */
	public static final String FORM = "platformcountForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "platformcount";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
}
