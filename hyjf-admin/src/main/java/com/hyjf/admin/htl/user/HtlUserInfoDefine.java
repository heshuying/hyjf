package com.hyjf.admin.htl.user;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class HtlUserInfoDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/htl/htlUserInfo";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "htl/htlUserInfo/htluserinfo";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	
	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	
	/** FROM */
	public static final String HTLUSERINFO_FORM = "htlUserInfoForm";

	/** FROM */
	public static final String EXEPORTEXECL = "exportExcel";

	/** 查看权限 */
	public static final String HTLUSERINFO = "htlUserInfo";

	/** 查看权限 */
	public static final String HTLUSERINFO_VIEW = HTLUSERINFO + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查询权限 */
	public static final String HTLUSERINFO_SEARCH = HTLUSERINFO + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String HTLUSERINFO_EXEPORTEXECL = HTLUSERINFO + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
