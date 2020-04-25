package com.hyjf.admin.exception.bindcardexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BindCardExceptionDefine extends BaseDefine {
	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/bindcardexception";
	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	/** 列表画面路径 @RequestMapping值 */
	public static final String LIST_PATH = "/exception/bindcardexception/bindcardexceptionlist";
	/** 检索Action @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	/** 更新Action @RequestMapping值 */
	public static final String MODIFY_ACTION = "modifyAction";
	/** form */
	public static final String FORM = "bindcardexceptionForm";
	/** 权限 @RequstMapping值 */
	public static final String PERMISSIONS = "bindcardexception";
	/** 查看权限 @RequestMapping值 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
	/** 检索权限 @RequestMapping值 */
	public static final String PERMISSION_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
	/** 更新权限 @RequestMapping值 */
	public static final String PERMISSION_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

}
