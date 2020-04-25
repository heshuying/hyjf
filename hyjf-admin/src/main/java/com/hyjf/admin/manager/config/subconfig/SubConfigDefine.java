package com.hyjf.admin.manager.config.subconfig;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class SubConfigDefine extends BaseDefine {

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/config/subconfig";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "manager/config/subconfig/subconfig";

    /** 迁移路径 */
    public static final String INFO_PATH = "manager/config/subconfig/subconfigDetail";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";

    /** 检索 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";

    /** 迁移到详细画面 @RequestMapping值 */
    public static final String INFO_ACTION = "infoAction";

    /** 数据添加Action @RequestMapping值 */
    public static final String INSERT_ACTION = "insertAction";

    /** 数据更新Action @RequestMapping */
    public static final String UPDATE_ACTION = "updateAction";

    /** 数据删除Action @RequestMapping */
    public static final String DELETE_ACTION = "deleteAction";
    
	/** 联动的 @RequestMapping值 */
	public static final String ASSETTYPE_ACTION = "/selectAction";
	/** 联动的 @RequestMapping值 */
	public static final String SEARCHUPDATE_ACTION = "/updateSelectAction";

    /** Form */
    public static final String FINMAN_CHARGE_FORM = "subconfigForm";

    /** 类名 */
    public static final String THIS_CLASS = SubConfigController.class.getName();

    /** 权限 */
    public static final String PERMISSIONS = "subconfig";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 删除权限 */
    public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

    /** 检索权限 */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /** 修改权限 */
    public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

    /** 添加权限 */
    public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;
}
