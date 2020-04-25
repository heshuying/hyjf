package com.hyjf.admin.manager.config.htlconfig;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class HtlConfigDefine extends BaseDefine {

    /** 惠天利配置 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/config/htlconfig";
    
    /** 惠天利配置 重定向 */
    public static final String RE_LIST_PATH = "redirect:/manager/config/htlconfig/init";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "manager/config/htlconfig/htlconfig";
    
    /** 列表画面迁移 */
    public static final String INFO_PATH = "manager/config/htlconfig/htlconfigInfo";

    /** 删除后 路径 */
    public static final String DELETE_AFTER_PATH = "manager/config/htlconfig/htlconfig";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";

    /** 迁移到详细画面 @RequestMapping值 */
    public static final String INFO_ACTION = "infoAction";

    /** 插入数据 @RequestMapping值 */
    public static final String INSERT_ACTION = "insertAction";

    /** 更新数据 @RequestMapping值 */
    public static final String UPDATE_ACTION = "updateAction";

    /** 二次提交后跳转的画面 */
    public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

    /** FROM */
    public static final String HTLCONFIG_FORM = "htlconfigForm";

	/** 权限关键字 */
	public static final String PERMISSIONS = "htlconfig";
	
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 详细权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;
}
