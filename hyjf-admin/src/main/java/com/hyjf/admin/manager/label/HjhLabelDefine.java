package com.hyjf.admin.manager.label;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class HjhLabelDefine extends BaseDefine {

    /** 标签配置 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/label";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "manager/label/hjhlabel";
    
    /** 提成配置 重定向 */
    public static final String RE_LIST_PATH = "redirect:/manager/label/init";

    /** 列表画面迁移 */
    public static final String INFO_PATH = "manager/label/hjhlabelInfo";

    /** 删除后 路径 */
    public static final String DELETE_AFTER_PATH = "manager/label/label";
    
    /** 列表画面 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";

    /** 迁移到详细画面 @RequestMapping值 */
    public static final String INFO_ACTION = "infoAction";
    
    /** 联动的 @RequestMapping值 */
    public static final String ASSETTYPE_ACTION = "/assetTypeAction";

    /** 插入数据 @RequestMapping值 */
    public static final String INSERT_ACTION = "insertAction";

    /** 更新数据 @RequestMapping值 */
    public static final String UPDATE_ACTION = "updateAction";

    /** 删除数据的 @RequestMapping值 */
    public static final String DELETE_ACTION = "deleteAction";
    
    /** 更新数据的 @RequestMapping值 */
    public static final String STATUS_ACTION = "statusAction";

    /** 二次提交后跳转的画面 */
    public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

    /** FROM */
    public static final String PUSHMONEY_FORM = "labelForm";

	/** 权限关键字 */
	public static final String PERMISSIONS = "label";
	
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
