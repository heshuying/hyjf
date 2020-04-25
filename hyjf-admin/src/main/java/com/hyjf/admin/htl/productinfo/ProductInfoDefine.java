package com.hyjf.admin.htl.productinfo;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class ProductInfoDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/htl/productinfo";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "htl/productinfo/productinfo";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	
	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	
	/** exeportExecl */
	public static final String EXPORTEXECL = "exportExcel";
	
	/** FROM */
	public static final String PRODUCTINFO_FORM = "productinfoForm";
	
	/** 查看权限 */
	public static final String PRODUCTINFO = "productinfo";

	/** 查看权限 */
	public static final String PRODUCTINFO_VIEW = PRODUCTINFO + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查询权限 */
	public static final String PRODUCTINFO_SEARCH = PRODUCTINFO + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PRODUCTINFO_EXPORTEXECL = PRODUCTINFO + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
}
