package com.hyjf.admin.htl.productinto;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class ProductIntoRecordCustomizeDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/htl/productinto/productintorecord";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "htl/productinto/productintorecord";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	
	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	
	/** exeportExecl */
	public static final String EXPORTEXECL = "exportExcel";
	
	/** FROM */
	public static final String PRODUCTINTORECORD_FORM = "productIntoRecordForm";
	
	/** 查看权限 */
	public static final String PRODUCTINTORECORD = "productIntoRecord";

	/** 查看权限 */
	public static final String PRODUCTINTORECORD_VIEW = PRODUCTINTORECORD + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查询权限 */
	public static final String PRODUCTINTORECORD_SEARCH = PRODUCTINTORECORD + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 删除权限 */
	public static final String PRODUCTINTORECORD_DELETE = PRODUCTINTORECORD + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PRODUCTINTORECORD_ADD = PRODUCTINTORECORD + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

	/** 导出权限 */
	public static final String PRODUCTINTORECORD_EXPORT = PRODUCTINTORECORD + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
}
