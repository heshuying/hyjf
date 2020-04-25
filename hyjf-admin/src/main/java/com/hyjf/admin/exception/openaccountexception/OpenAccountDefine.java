package com.hyjf.admin.exception.openaccountexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class OpenAccountDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/openaccount";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "exception/openaccountexception/openAccountList";
	
	/** 列表画面 路径 */
	public static final String UPDATE_ACCOUNT_PATH = "exception/openaccountexception/updateAccount";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT_ACTION = "initAction";
	
	 /** 查询数据 @RequestMapping值 */
    public static final String UPDATE_ACCOUNT_LOG_ACTION = "updateAccountLogAction";

    /** 查询数据 @RequestMapping值 */
    public static final String INIT_UPDATE_ACCOUNT_ACTION = "initUpdateAccountAction";
    
    /** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACCOUNT_ACTION = "updateAccountAction";
    
	/** FROM */
	public static final String ACCOUNT_LIST_FORM = "accountListForm";
	
	/** FROM */
	public static final String UPDATE_ACCOUNT_FORM = "updateAccountForm";
	
	/** 查看权限 */
	public static final String PERMISSIONS = "openaccountexception";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 删除权限 */
	public static final String PERMISSION_CONFIRM_ACCOUNT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_CONFIRM_ACCOUNT;

	  /**
     * 类名
     */
	public static final String THIS_CLASS = OpenAccountController.class.getName();

}
