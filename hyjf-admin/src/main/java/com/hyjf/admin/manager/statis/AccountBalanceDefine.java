package com.hyjf.admin.manager.statis;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class AccountBalanceDefine extends BaseDefine{
	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/statis";

	/** 列表画面  */
	public static final String INIT = "init";
	 
    /** 导出信息  */
    public static final String HJHACCOUNTBALANCE_EXPORT = "exportAction";
    
    /** 按月导出信息  */
    public static final String HJHACCOUNTBALANCE_Month_EXPORT = "exportActionMonth";
    
    /** 导出信息  */
    public static final String SEARCH_ACTION = "searchAction";
    
    
    /** 日列表画面 路径 */
    public static final String LIST_PATH = "/manager/statis/hjhAccountBalance";
    
    /** 月列表画面 路径 */
    public static final String MONTH_LIST_PATH = "/manager/statis/hjhAccountBalanceMonth";
	
	/** FORM */
	public static final String HJHACCOUNTBALANCE_FORM = "HjhAccountBalanceForm";
	   
    /** 查看权限 */
    public static final String PERMISSIONS = "hjhstatis";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 查询权限 */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /** 修改权限 */
    public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

    /** 删除权限 */
    public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;
   
    /** 添加权限 */
    public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;
   
    /** 导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
}
