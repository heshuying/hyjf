package com.hyjf.admin.finance.merchant.account;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class MerchantAccountDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/finance/merchant/account";

	/** 用户转账列表 @RequestMapping值 */
	public static final String ACCOUNT_LIST_ACTION = "accountList";

	/** 用户转账列表导出 @RequestMapping值 */
	public static final String EXPORT_ACCOUNT_ACTION = "exportAccount";

	/** 用户转账列表FROM */
	public static final String ACCOUNT_LIST_FORM = "accountListForm";

	/** 用户转账确认FROM */
	public static final String ACCOUNT_FORM = "accountForm";
	
	/** 用户转账列表 */
	public static final String ACCOUNT_LIST_PATH = "finance/merchant/account/accountList";
	
	/** 权限名称 */
	public static final String PERMISSIONS = "merchantaccountlist";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 添加权限 */
	public static final String PERMISSION_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 类名 */
	public static final String THIS_CLASS = MerchantAccountController.class.getName();

}
