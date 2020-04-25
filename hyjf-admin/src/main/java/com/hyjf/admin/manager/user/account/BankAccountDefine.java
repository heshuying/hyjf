package com.hyjf.admin.manager.user.account;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BankAccountDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/bankaccount";


	/*****************************************银行存管相关     pcc   start***************************************************/
	/** 用户开户记录 */
    public static final String BANK_ACCOUNT_LIST_PATH = "manager/users/accountlist/bankAccountList";
    /** 会员开户列表画面 @RequestMapping值 */
    public static final String BANK_ACCOUNT_LIST_ACTION = "accountlist";
    
    /** 获取会员管理列表 @RequestMapping值 */
    public static final String EXPORT_BANK_ACCOUNT_ACTION = "exportbankaccount";
	/*****************************************银行存管相关     pcc   end***************************************************/

	/** 用户开户列表FROM */
	public static final String ACCOUNT_LIST_FORM = "accountListForm";
	
	/** 查看权限 */
	public static final String PERMISSIONS = "accountlist";
	
	/** 文件导出权限 */
	public static final String PERMISSIONS_OPEN_ACCOUNT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_CONFIRM_ACCOUNT;

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
	
	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
	
	/** 类名*/
	public static final String THIS_CLASS = AccountController.class.getName();

}
