package com.hyjf.admin.manager.config.operationlog;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class OperationLogDefine extends BaseDefine {

    /** 配置 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/config/operationlog";
    
    /** 配置 重定向 */
    public static final String RE_LIST_PATH = "redirect:/manager/config/operationlog/init";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "manager/config/operationlog/operationlog";
    
    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";

    /** 迁移到详细画面 @RequestMapping值 */
    public static final String INFO_ACTION = "infoAction";

    /** 二次提交后跳转的画面 */
    public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

    /** FROM */
    public static final String HTLCONFIG_FORM = "operationlogForm";
    
    /** 导出数据 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

	/** 权限关键字 */
	public static final String PERMISSIONS = "operationlog";
	
	/** 权限关键字list */
	public static final String PERMISSIONS_FROM = "operationloglist";
	
	
	
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 详细权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS_FROM + StringPool.COLON + ShiroConstants.PERMISSION_INFO;
}
