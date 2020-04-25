package com.hyjf.admin.app.maintenance.pushmanage;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * @Author : huanghui
 */
public class AppPushManageDefine extends BaseDefine {

    /** 推送列表 CONTROLLOR @RequestMapping值*/
    public static final String REQUEST_MAPPING = "/app/maintenance/pushmanage";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "app/maintenance/pushmanage/pushmanage";

    /** 画面迁移 路径 */
    public static final String INFO_PATH = "app/maintenance/pushmanage/pushmanageInfo";

    /** 从定向 路径 */
    public static final String RE_LIST_PATH = "redirect:/app/maintenance/pushmanage/init";

    /** 删除后 路径 */
    public static final String DELETE_AFTER_PATH = "app/maintenance/pushmanage/pushmanage";

    /** FROM */
    public static final String APP_PUSH_MANAGE_FORM = "appPushManageForm";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";

    /** 迁移到详细画面 @RequestMapping值 */
    public static final String INFO_ACTION = "infoAction";

    /** 条件查询数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";

    /** 插入数据 @RequestMapping值 */
    public static final String INSERT_ACTION = "insertAction";

    /** 更新数据 @RequestMapping值 */
    public static final String UPDATE_ACTION = "updateAction";

    /** 删除数据的 @RequestMapping值 */
    public static final String DELETE_ACTION = "deleteAction";

    /**CheckAction*/
    public static final String CHECK_ACTION = "checkAction";

    /**更新状态动作*/
    public static final String STATUS_ACTION = "statusAction";

    /**上传操作*/
    public static final String UPLOAD_FILE = "uploadFile";

    /** 权限关键字 */
    public static final String PERMISSIONS = "apppushmanage";

    public static final String ERROR_STATUS = "error";

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
