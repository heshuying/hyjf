package com.hyjf.admin.maintenance.role;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class AdminRoleDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/maintenance/role";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "maintenance/role/role";

	/** 详细画面的路径 */
	public static final String INFO_PATH = "maintenance/role/roleInfo";

    /** 授权画面的路径 */
    public static final String AUTH_PATH = "maintenance/role/authInfo";

	/** 删除后 路径 */
	public static final String DELETE_AFTER_PATH = "maintenance/role/permissions";

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

    /** 迁移到授权画面 @RequestMapping值 */
    public static final String AUTH_ACTION = "authAction";

    /** 获取菜单列表信息 @RequestMapping值 */
    public static final String MENU_INFO_ACTION = "menuInfoAction";

    /** 插入或更新[角色菜单权限表]数据 @RequestMapping值 */
    public static final String MODIFY_PERMISSION_ACTION = "modifyPermissionAction";

    /** 检查角色名称是否唯一 @RequestMapping值 */
    public static final String CHECK_ACTION = "checkAction";

	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;
	
	/** FROM */
	public static final String ROLE_FORM = "roleForm";
	
	/** 查看权限 */
	public static final String ROLE = "role";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = ROLE + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = ROLE + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = ROLE + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = ROLE + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

    /** 授权权限 */
    public static final String PERMISSIONS_AUTH = ROLE + StringPool.COLON + ShiroConstants.PERMISSION_AUTH;


}
