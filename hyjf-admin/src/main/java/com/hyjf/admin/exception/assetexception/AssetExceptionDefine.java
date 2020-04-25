package com.hyjf.admin.exception.assetexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * @author PC-LIUSHOUYI
 * @version AssetExceptionCustomize, v0.1 2018/8/2 9:39
 */
public class AssetExceptionDefine extends BaseDefine {

    /**
     * 权限 CONTROLLOR @RequestMapping值
     */
    public static final String REQUEST_MAPPING = "/exception/assetexception";

    /** 列表画面迁移 */
    public static final String INFO_PATH = "/exception/assetexception/assetexceptionInfo";

    /** 迁移到详细画面 @RequestMapping值 */
    public static final String INFO_ACTION = "infoAction";

    /**
     * 列表画面 路径
     */
    public static final String LIST_PATH = "/exception/assetexception/assetexception";

    /** 删除 重定向 */
    public static final String RE_LIST_PATH = "redirect:/exception/assetexception/init";

    /**
     * 删除后 路径
     */
    public static final String DELETE_AFTER_PATH = "/exception/assetexception/assetexception";

    /**
     * 列表画面 @RequestMapping值
     */
    public static final String INIT = "init";

    /**
     * 列表画面 @RequestMapping值
     */
    public static final String SEARCH_ACTION = "searchAction";

    /**
     * 删除数据的 @RequestMapping值
     */
    public static final String DELETE_ACTION = "deleteAction";

    /**
     * 删除数据的 @RequestMapping值
     */
    public static final String UPDATE_ACTION = "updateAction";

    /**
     * 删除数据的 @RequestMapping值
     */
    public static final String INSERT_ACTION = "insertAction";

    /**
     * 标的异常导出 @RequestMapping
     */
    public static final String EXPORT_ACTION = "exportAction";

    /**
     * 二次提交后跳转的画面
     */
    public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

    /**
     * FROM
     */
    public static final String ASSET_FORM = "assetexceptionForm";

    /**
     * 查看权限
     */
    public static final String PERMISSIONS = "assetexception";

    /**
     * 判断项目编号是否存在
     */
    public static final String ISEXISTSNID_ACTION = "isExistsBorrowNid";

    /**
     * 查看权限
     */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /**
     * 检索权限
     */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /**
     * 删除权限
     */
    public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

    /**
     * 编辑权限
     */
    public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

    /**
     * 添加权限
     */
    public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

    /**
     * 导出权限
     */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

    /** 详细权限 */
    public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;
}
