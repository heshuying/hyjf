package com.hyjf.admin.htl.productredeemdetail;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class ProductRedeemDetailDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/htl/productredeemdetail";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "htl/productredeem/productredeemdetail";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	
	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	
	/** FROM */
	public static final String PRODUCTREDEEMDETAIL_FORM = "productredeemdetailForm";
	
	/** 查看权限 */
	public static final String PRODUCTREDEEMDETAIL = "productredeemdetail";

	/** 查看权限 */
	public static final String PRODUCTREDEEMDETAIL_VIEW = PRODUCTREDEEMDETAIL + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查询权限 */
	public static final String PRODUCTREDEEMDETAIL_SEARCH = PRODUCTREDEEMDETAIL + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
	
	/** 添加权限 */
	public static final String PRODUCTREDEEMDETAIL_ADD = PRODUCTREDEEMDETAIL + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

}
