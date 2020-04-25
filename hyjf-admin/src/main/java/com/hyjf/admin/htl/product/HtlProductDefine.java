package com.hyjf.admin.htl.product;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class HtlProductDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/htl/product";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "htl/product/product";

	/** 详细画面的路径 */
	public static final String INFO_PATH = "htl/product/productInfo";

	/** 删除后 路径 */
	public static final String DELETE_AFTER_PATH = "htl/product/product";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;
	
	/** FROM */
	public static final String PRODUCT_FORM = "productForm";
	
	/** 查看权限 */
	public static final String PRODUCT = "product";

	/** 查看权限 */
	public static final String PRODUCT_VIEW = PRODUCT + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PRODUCT_MODIFY = PRODUCT + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PRODUCT_DELETE = PRODUCT + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PRODUCT_ADD = PRODUCT + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

}
