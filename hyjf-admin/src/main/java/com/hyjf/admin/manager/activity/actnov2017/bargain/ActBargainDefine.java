package com.hyjf.admin.manager.activity.actnov2017.bargain;

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
public class ActBargainDefine extends BaseDefine {

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/activity/actnov/bargain";

    /** 列表画面 路径 */
    public static final String LIST_PATH_BARGAIN = "manager/activity/act2017/actnov/bargainList";
    
    /** 列表画面 路径 */
    public static final String LIST_PATH_PRIZEWIN = "manager/activity/act2017/actnov/prizeWinList";
    
    /** 列表画面 @RequestMapping值 */
    public static final String INIT_BARGAIN = "initBargain";
    
    /** 列表画面 @RequestMapping值 */
    public static final String INIT_PRIZEWIN = "initPrizeWin";
    
    /** 检索数据 @RequestMapping值 */
    public static final String SEARCH_BARGAIN_ACTION = "searchBargainAction";
    
    /** FROM */
    public static final String ACTNOV_BARGAIN_LIST_FORM = "bargainListForm";

    /** 查看权限 */
    public static final String PERMISSIONS = "activitylist";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 查找权限 */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /** 导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
