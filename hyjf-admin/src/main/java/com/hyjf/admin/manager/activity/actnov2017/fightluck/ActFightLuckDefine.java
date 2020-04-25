package com.hyjf.admin.manager.activity.actnov2017.fightluck;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 十一月份活动
 * @author Michael
 */
public class ActFightLuckDefine extends BaseDefine {

    /** 请求 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/activity/nov/actFightLuck";
    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "manager/activity/act2017/actnov/fightlucklist";

    /** 查看权限 */
    public static final String PERMISSIONS = "activitylist";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;


}
