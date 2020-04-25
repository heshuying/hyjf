package com.hyjf.admin.manager.activity.qixiactivity2018;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * @author by xiehuili on 2018/7/23.
 */
public class QixiActivityDefine  extends BaseDefine {


    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/activity/qixiactivity";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "manager/activity/qixiactivity2018/qixiactivity";
    /** 列表画面 路径 */
    public static final String TOTAL_LIST_PATH = "manager/activity/qixiactivity2018/qixiactivitytotal";
    /**奖励明细 列表画面 路径 */
    public static final String AWARD_LIST_PATH = "manager/activity/qixiactivity2018/qixiactivityaward";
    /**奖励明细 列表画面 路径 */
    public static final String AWARD_INFO_PATH = "manager/activity/qixiactivity2018/qixiactivityawardinfo";
//
//    /** 列表画面 路径 */
//    public static final String LIST_OPPORTUNITY_PATH = "manager/activity/prize/prizeopportunity";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    /** 累计出借列表画面 @RequestMapping值 */
    public static final String TOTAL_INIT = "totalinit";

    /** 奖励明细列表画面 @RequestMapping值 */
    public static final String AWARD_INIT = "awardinit";

    /** 奖励明细修改状态 @RequestMapping值 */
    public static final String INFO_ACTION = "infoAction";

    /** 奖励明细修改状态 @RequestMapping值 */
    public static final String UPDATE_ACTION = "updateAction";
    /** 从定向 路径 */
    public static final String RE_LIST_PATH = "redirect:" + AWARD_INIT;

    /** 查看权限 */
    public static final String PERMISSIONS = "activitylist";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
    /** 查看权限 */
    public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

    /** FROM */
    public static final String PRIZECODE_FORM = "qixiactivityListForm";
    public static final String EXPORT_EXCEL_ACTION = "exportExcelAction";
    public static final String EXPORT_EXCEL_TOTAL_ACTION = "exportExcelTotalAction";
    public static final String EXPORT_EXCEL_AWARD_ACTION = "exportExcelAwardAction";
    /**
     * 导出权限
     */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
