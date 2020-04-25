package com.hyjf.admin.finance.bank.merchant.poundage;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BankPoundageAccountDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/merchant/poundage";

	/** 用户转账列表 @RequestMapping值 */
	public static final String INIT = "init";

	/** 用户转账列表FROM */
	public static final String ACCOUNT_LIST_FORM = "form";
	
	/** 用户转账列表 */
	public static final String ACCOUNT_LIST_PATH = "bank/merchant/poundage/poundageaccountlist";
	
	/** 权限名称 */
	public static final String PERMISSIONS = "bankpoundageaccount";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
	/** 查看权限 */
    public static final String PERMISSION_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;
	/** 文件导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 类名 */
	public static final String THIS_CLASS = BankPoundageAccountController.class.getName();

}
