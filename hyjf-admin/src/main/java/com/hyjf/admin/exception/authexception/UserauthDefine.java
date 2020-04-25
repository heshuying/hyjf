package com.hyjf.admin.exception.authexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class UserauthDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/authexception";

	/** 用户授权明细 */
	public static final String USER_AUTH_LIST_PATH = "exception/authexception/userauthList";

	/** 会员自动出借授权列表画面 @RequestMapping值 */
	public static final String USERAUTH_LIST_ACTION = "userauthlist";

	/** 授权状态同步 */
	public static final String USERAUTH_QUERY_ACTION = "userauthquery";
	/** 自动投标解约 */
	public static final String USER_INVEST_CANCEL_ACTION = "userinvescancel";
	/** 自动债转解约 */
	public static final String USER_CREDIT_CANCEL_ACTION = "usercreditcancel";
	/** 操作之后重定向 */
	public static final String RE_LIST_PATH = "redirect:/manager/userauth/userauthlist";

	
	/** 获取会员授权列表 @RequestMapping值 */
	public static final String EXPORT_USERAUTH_ACTION = "exportuserauth";
	
	/** 用户授权列表FROM */
	public static final String USERAUTH_LIST_FORM = "userauthListForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "exceptionuserauth";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
	
	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
	
	/** 数据脱敏权限*/
	public static final String PERMISSION_HIDE_SHOW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_HIDDEN_SHOW;
	
	/** 类名*/
	public static final String THIS_CLASS = UserauthExceptionController.class.getName();

	/**用户授权同步*/
    public static final String USERAUTH_SYN_ACTION = "userauthSyn";

}
