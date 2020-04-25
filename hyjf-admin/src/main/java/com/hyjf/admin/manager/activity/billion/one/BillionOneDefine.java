package com.hyjf.admin.manager.activity.billion.one;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 十一月份活动
 * @author Michael
 */
public class BillionOneDefine extends BaseDefine {

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/activity/billion/one";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "manager/activity/billion/billion_one";
    
    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    
    /** 导出数据 @RequestMapping值 */
    public static final String EXPORT_ACTION = "exportAction";
    
    /** 检索数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";
    
    /** FROM */
    public static final String FORM = "billionOneForm";

    /** 查看权限 */
    public static final String PERMISSIONS = "activitylist";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 查找权限 */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /** 导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
