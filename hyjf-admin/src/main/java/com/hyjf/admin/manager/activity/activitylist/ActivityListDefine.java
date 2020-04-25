package com.hyjf.admin.manager.activity.activitylist;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class ActivityListDefine extends BaseDefine {

    /** 活动列表 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/activity/activitylist";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "manager/activity/activitylist/activitylist";

    /** 从定向 路径 */
    public static final String RE_LIST_PATH = "redirect:/manager/activity/activitylist/init";

    /** 迁移 路径 */
    public static final String INFO_PATH = "manager/activity/activitylist/activitylistInfo";

    /** 删除后 路径 */
    public static final String DELETE_AFTER_PATH = "manager/activity/activitylist/activitylist";

    /** 活动详情路径 */
    public static final String ACTIVITY_INFO_LIST_PATH = "manager/activity/activitylist/activityInfoList";

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

    public static final String UPLOAD_FILE = "uploadFile";

    /** 活动详情@RequestMapping值 */
    public static final String ACTIVITY_INFO_ACTION = "getActivityInfoAction";

    /** 活动返现@RequestMapping值 */
    public static final String RETURNCASH_ACTION = "returncashAction";

    public static final String ACTIVITY_INFO = "getActivityInfo";

    /** 二次提交后跳转的画面 */
    public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

    /** FROM */
    public static final String ACTIVITYLIST_FORM = "activitylistForm";

    public static final String ACTIVITY_INFO_FORM = "activityinfoForm";

    /** 权限关键字 */
    public static final String PERMISSIONS = "activitylist";

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

    /** 返手续费权限 */
    public static final String PERMISSIONS_RETURNCASH_RETURNCASH = PERMISSIONS + StringPool.COLON
            + ShiroConstants.PERMISSION_RETURNCASH;

}
