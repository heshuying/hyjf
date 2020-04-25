package com.hyjf.admin.maintenance.admin;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class AdminDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/maintenance/admin";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "maintenance/admin/admin";

	/** 详细画面的路径 */
	public static final String INFO_PATH = "maintenance/admin/adminInfo";

	/** 删除后 路径 */
	public static final String DELETE_AFTER_PATH = "maintenance/admin/admin";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

    /** 查询数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";
    
	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";

    /** 检查是否唯一 @RequestMapping值 */
    public static final String CHECK_ACTION = "checkAction";
    
    /** 重置密码 @RequestMapping值 */
    public static final String RESETPWD_ACTION = "resetPwdAction";
    
	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;
	
	/** FROM */
	public static final String ADMIN_FORM = "adminForm";
	
	/** 查看权限 */
	public static final String PERMISSIONS = "admin";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;
	
	/** 删除权限 */
    public static final String PERMISSIONS_RESETPWD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_RESETPWD;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

    /** 设置角色权限 */
    public static final String PERMISSIONS_AUTH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_AUTH;
    
    /** 状态(不可用) */
    public static final String FLG_DISABLE = "1";

    /** 状态(可用) */
    public static final String FLG_AVTIVE = "0";

}
