package com.hyjf.admin.manager.activity.newuser;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 
 * 九月份运营新手活动
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月26日
 * @see 上午9:57:02
 */
public class NewUserActivityDefine extends BaseDefine {

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/activity/newuser";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "manager/activity/newuser/newuser";
    
    /** 全站注册用户送券活动 路径 */
    public static final String LIST_PATH_REGISTALL = "manager/activity/newuser/newuserall";
    
    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    
    /** 全站注册用户送券活动  @RequestMapping值 */
    public static final String INIT_REGISTALL = "initAll";
    
    /** 导出数据 @RequestMapping值 */
    public static final String EXPORT_ACTION = "exportAction";
    
    /** 全站活动导出数据 @RequestMapping值 */
    public static final String EXPORT_ALL_ACTION = "exportAllAction";
    
    /** 导出数据 全站注册用户送券活动 @RequestMapping值 */
    public static final String EXPORT_ACTION_REGISTALL = "exportActionAll";
    
    /** 检索数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";
    
    /** 检索数据 全站注册用户送券活动  @RequestMapping值 */
    public static final String SEARCH_ACTION_REGISTALL = "searchActionAll";
    
    /** FROM */
    public static final String NEWUSER_FORM = "newUserListForm";

    /** 查看权限 */
    public static final String PERMISSIONS = "activitylist";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 查找权限 */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /** 导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
