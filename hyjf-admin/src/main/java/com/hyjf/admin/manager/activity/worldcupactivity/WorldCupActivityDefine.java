package com.hyjf.admin.manager.activity.worldcupactivity;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * @author xiehuili on 2018/6/13.
 */
public class WorldCupActivityDefine extends BaseDefine {

    /**
     * 权限关键字
     */
    public static final String PERMISSIONS = "worldCupActivity";
    /**
     * 查看权限
     */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /**
     * 检索权限
     */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /**
     * 导出权限
     */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

    /**
     * 世界杯活动 @RequestMapping值
     */
    public static final String REQUEST_MAPPING = "/manager/activity/worldCupActivity";

    /**
     * 列表画面 @RequestMapping值
     */
    public static final String INIT = "init";

    /**
     * 竞猜冠军列表画面 @RequestMapping值
     */
    public static final String WINNING_INIT = "winningInit";

    /**
     * 列表画面 路径
     */
    public static final String LIST_PATH = "manager/activity/worldCupActivity/worldCupActivity";

    /**
     * 竞猜冠军列表画面 路径
     */
    public static final String WINNING_LIST_PATH = "manager/activity/worldCupActivity/worldCupActivitywinning";


    /**
     * 条件查询数据 @RequestMapping值
     */
    public static final String SEARCH_ACTION = "searchAction";

    /**
     * FROM
     */
    public static final String FORM = "worldCupActivityForm";

    public static final String EXPORT_EXCEL_ACTION = "exportExcelAction";

    public static final String EXPORT_WINNING_EXCEL_ACTION = "exportWinningExcelAction";

}
