package com.hyjf.admin.htl.productredeem;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class ProductRedeemDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/htl/productredeem";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "htl/productredeem/productredeem";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	
	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	/** 导出execl @RequestMapping值 */
	public static final String EXPORTEXECL = "exportExcel";
	
	/** FROM */
	public static final String PRODUCTREDEEMCUSTOMIZE_FORM = "productredeemcustomizeForm";
	
	/** 查看权限 */
	public static final String PRODUCTREDEEMCUSTOMIZE = "productredeem";

	/** 查看权限 */
	public static final String PRODUCTREDEEMCUSTOMIZE_VIEW = PRODUCTREDEEMCUSTOMIZE + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查询权限 */
	public static final String PRODUCTREDEEMCUSTOMIZE_SEARCH = PRODUCTREDEEMCUSTOMIZE + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
	
	/** 导出权限 */
	public static final String PRODUCTREDEEMCUSTOMIZE_EXEPORTEXECL = PRODUCTREDEEMCUSTOMIZE + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
