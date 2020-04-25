package com.hyjf.admin.manager.activity.doubleSection;

import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * @Auther: walter.limeng
 * @Date: 2018/9/11 09:51
 * @Description: DoubleSectionActivityDefine
 */
public class DoubleSectionActivityDefine {
    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/activity/doublesectionactivity";
    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";

    /** 查看权限 */
    public static final String PERMISSIONS = "activitylist";
    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;
    /** 列表画面 路径 */
    public static final String LIST_PATH = "manager/activity/doublesection/sectionactivity";
    /** FROM */
    public static final String PRIZECODE_FORM = "dousectionactivityListForm";

    /** 奖励明细列表画面 @RequestMapping值 */
    public static final String AWARD_INIT = "awardinit";
    /**奖励明细 列表画面 路径 */
    public static final String AWARD_LIST_PATH = "manager/activity/doublesection/sectionactivityaward";

    /** 奖励明细修改状态 @RequestMapping值 */
    public static final String INFO_ACTION = "infoAction";
    /**奖励明细 列表画面 路径 */
    public static final String AWARD_INFO_PATH = "manager/activity/doublesection/sectionactivityawardinfo";
    /** 奖励明细修改状态 @RequestMapping值 */
    public static final String UPDATE_ACTION = "updateAction";
    /** 从定向 路径 */
    public static final String RE_LIST_PATH = "redirect:" + AWARD_INIT;

    public static final String EXPORT_EXCEL_ACTION = "exportExcelAction";
    public static final String EXPORT_EXCEL_AWARD_ACTION = "exportExcelAwardAction";

    /**
     * 导出权限
     */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;
}
