package com.hyjf.admin.exception.mobilesynchronize;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class MobileSynchronizeDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/mobilesynchronize";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 列表路径 @RequestMapping值 */
	public static final String LIST_PATH = "/exception/mobilesynchronize/mobilesynchronizelist";

	/** 列表检索 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 修改手机号 @RequestMapping值 */
	public static final String MODIFY_ACTION = "modifyAction";

	/** FORM */
	public static final String FORM = "mobilesynchronizeForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "mobilesynchronize";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

}
