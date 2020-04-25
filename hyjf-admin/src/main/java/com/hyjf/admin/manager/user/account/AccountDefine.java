package com.hyjf.admin.manager.user.account;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class AccountDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/account";

	/** 用户开户记录 */
	public static final String ACCOUNT_LIST_PATH = "manager/users/accountlist/accountList";
	
	/** 更新开户信息 */
	public static final String OPEN_ACCOUNT_PATH = "manager/users/accountlist/openAccount";
	
	/** 会员开户列表画面 @RequestMapping值 */
	public static final String ACCOUNT_LIST_ACTION = "accountlist";
	
	/** 获取会员管理列表 @RequestMapping值 */
	public static final String EXPORT_ACCOUNT_ACTION = "exportaccount";
	
	/** 用户开户*/
	public static final String INIT_OPEN_ACCOUNT_ACTION = "initopenaccount";
	
	/** 用户开户*/
	public static final String OPEN_ACCOUNT_ACTION = "openaccount";
	

	/** 用户开户列表FROM */
	public static final String ACCOUNT_LIST_FORM = "accountListForm";
	
	/** 用户开户确认FROM */
	public static final String ACCOUNT_FORM = "accountForm";
	
	/** 查看权限 */
	public static final String PERMISSIONS = "accountlist";
	
	/** 文件导出权限 */
	public static final String PERMISSIONS_OPEN_ACCOUNT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_CONFIRM_ACCOUNT;

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
	
	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
	
	/** 数据脱敏权限 */
	public static final String PERMISSION_HIDE_SHOW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_HIDDEN_SHOW;
	
	/** 类名*/
	public static final String THIS_CLASS = AccountController.class.getName();

}
