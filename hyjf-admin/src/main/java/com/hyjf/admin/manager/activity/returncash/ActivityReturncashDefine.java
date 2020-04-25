package com.hyjf.admin.manager.activity.returncash;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class ActivityReturncashDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/activity/returncash";

	/** 待返现列表画面 路径 */
	public static final String RETURNCASH_LIST_PATH = "manager/activity/returncash/returncash";

	/** 已返现列表画面 路径 */
	public static final String RETURNEDCASH_LIST_PATH = "manager/activity/returncash/returnedcash";

    /** 待返现列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    
	/** 待返现列表画面 @RequestMapping值 */
	public static final String RETURNCASH = "returncash";

    /** 已返现列表画面 @RequestMapping值 */
    public static final String RETURNEDCASH = "returnedcash";

    /** 查询待返现列表数据 @RequestMapping值 */
    public static final String SEARCH_RETURNCASH_ACTION = "searchReturncashAction";

    /** 查询已返现列表数据 @RequestMapping值 */
    public static final String SEARCH_RETURNEDCASH_ACTION = "searchReturnedcashAction";
    
	/** 执行返现操作 @RequestMapping值 */
	public static final String RETURNCASH_ACTION = "returncashAction";
    
    /** 调用汇付接口后的回调Action @RequestMapping值 */
    public static final String RETURNCASH_CALLBACK_ACTION = "callbackAction";
    
    /** 导出待返现列表 @RequestMapping值 */
    public static final String EXPORT_RETURNCASH_ACTION = "exportReturncashExcelAction";
    
    /** 导出已返现列表 @RequestMapping值 */
    public static final String EXPORT_RETURNEDCASH_ACTION = "exportReturnedcashExcelAction";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + RETURNCASH;
	
	/** FROM */
	public static final String RETURNCASH_FORM = "returncashForm";
	
	/** 查看权限 */
	public static final String PERMISSIONS_RETURNCASH = "returncashlist";

	/** 查看权限 */
	public static final String PERMISSIONS_RETURNCASH_VIEW = PERMISSIONS_RETURNCASH + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 返手续费权限 */
    public static final String PERMISSIONS_RETURNCASH_RETURNCASH = PERMISSIONS_RETURNCASH + StringPool.COLON + ShiroConstants.PERMISSION_RETURNCASH;

    /** 文件导出权限 */
    public static final String PERMISSIONS_RETURNCASH_EXPORT = PERMISSIONS_RETURNCASH + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
    
    /** 查看权限 */
    public static final String PERMISSIONS_RETURNEDCASH = "returnedcashlist";

    /** 查看权限 */
    public static final String PERMISSIONS_RETURNEDCASH_VIEW = PERMISSIONS_RETURNEDCASH + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 文件导出权限 */
    public static final String PERMISSIONS_RETURNEDCASH_EXPORT = PERMISSIONS_RETURNEDCASH + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
}
