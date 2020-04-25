package com.hyjf.admin.manager.user.userauth;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class UserauthLogDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/userauth";

	/** 用户授权明细 */
	public static final String USER_AUTH_LIST_PATH = "manager/users/userauthlist/userauthlogList";

	/** 会员自动出借授权列表画面 @RequestMapping值 */
	public static final String USERAUTH_LIST_ACTION = "userauthloglist";
	
	/** 获取会员授权列表 @RequestMapping值 */
	public static final String EXPORT_USERAUTH_ACTION = "exportuserauthlog";
	
	/** 用户授权列表FROM */
	public static final String USERAUTH_LIST_FORM = "userauthLogListForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "userauthlist";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
	
	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
	
	/** 数据脱敏权限*/
	public static final String PERMISSION_HIDE_SHOW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_HIDDEN_SHOW;
	
	/** 类名*/
	public static final String THIS_CLASS = UserauthLogController.class.getName();

}
