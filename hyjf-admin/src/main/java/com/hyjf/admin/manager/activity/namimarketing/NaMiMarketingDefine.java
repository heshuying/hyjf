package com.hyjf.admin.manager.activity.namimarketing;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * @author xiehuili on 2018/11/8.
 */
public class NaMiMarketingDefine extends BaseDefine {

    /** 查看权限 */
    public static final String PERMISSIONS = "activitylist";

    /** FROM */
    public static final String PRIZECODE_FORM = "naMiMarketingForm";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/activity/namimarketing";

    /** 邀请明细列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    /** 业绩返现详情列表画面 @RequestMapping值 */
    public static final String PERFORMANCE_INIT = "performanceInit";
    /** 邀请人返现明细列表画面 @RequestMapping值 */
    public static final String REFFER_DETAIL_INIT = "refferDetailInit";
    /** 邀请人返现统计列表画面 @RequestMapping值 */
    public static final String REFFER_TOTAL_INIT = "refferTotalInit";


    /** 业绩返现详情详情@RequestMapping值 */
    public static final String PERFORMANCE_INFO = "performanceInfo";

    //    /** 邀请明细 列表画面 路径 */
    public static final String LIST_PATH = "manager/activity/namimarketing/init";
    /** 业绩返现详情 列表画面 路径 */
    public static final String PERFORMANCE_LIST_PATH = "manager/activity/namimarketing/performance";
    /** 业绩返现详情 详情页面 路径 */
    public static final String PERFORMANCE_INFO_LIST_PATH = "manager/activity/namimarketing/performanceInfo";

    /**邀请人返现明细 列表画面 路径 */
    public static final String REFFER_DETAIL_LIST_PATH = "manager/activity/namimarketing/refferDetail";
    /**邀请人返现统计 列表画面 路径 */
    public static final String REFFER_TOTAL_LIST_PATH = "manager/activity/namimarketing/refferTotal";


    /** 邀请明细 列表画面 路径 */
    public static final String EXPORT_ACTION = "exportAction";
    /** 业绩返现详情 列表画面 路径 */
    public static final String EXPORT_PERFORMANCE_ACTION = "exportPerformanceAction";
    /**邀请人返现明细 列表画面 路径 */
    public static final String EXPORT_REFFER_DETAIL_ACTION = "exportRefferDetailAction";
    /**邀请人返现明细 列表画面 路径 */
    public static final String EXPORT_REFFER_TOTAL_ACTION = "exportRefferTotalAction";

    /**
     * 导出权限
     */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}

