package com.hyjf.admin.htl.productinterest;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class ProductInterestDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/htl/productinterest";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "htl/productinterest/productinterest";

	/** exeportExecl */
	public static final String EXPORTEXECL = "exportExcel";
	
	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;
	
	/** FROM */
	public static final String PRODUCTINTERESTCUSTOMIZE_FORM = "productinterestcustomizeForm";
	
	/** 查看权限 */
	public static final String PRODUCTINTERESTCUSTOMIZE = "productinterest";

	/** 查看权限 */
	public static final String PRODUCTINTERESTCUSTOMIZE_VIEW = PRODUCTINTERESTCUSTOMIZE + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查询权限 */
	public static final String PRODUCTINTERESTCUSTOMIZE_SEARCH = PRODUCTINTERESTCUSTOMIZE + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PRODUCTINTERESTCUSTOMIZE_EXEPORTEXECL = PRODUCTINTERESTCUSTOMIZE + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
