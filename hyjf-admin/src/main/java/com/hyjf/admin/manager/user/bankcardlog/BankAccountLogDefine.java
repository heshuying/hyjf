package com.hyjf.admin.manager.user.bankcardlog;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class BankAccountLogDefine extends BaseDefine {

    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/user/bankaccountlog";
  
    /** 列表画面 路径 */
    public static final String LIST_PATH = "manager/users/bankaccountlog/bankaccountlogList";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    
    /** 条件查询数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";

    /** 迁移到详细画面 @RequestMapping值 */
    public static final String EXPORT_ACTION = "exportAction";

    /** FROM */
    public static final String FORM = "bankaccountlogForm";

	/** 权限关键字 */
	public static final String PERMISSIONS = "bankaccountlog";
	
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
