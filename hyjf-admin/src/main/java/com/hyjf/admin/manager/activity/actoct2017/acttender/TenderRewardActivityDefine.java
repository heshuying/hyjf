package com.hyjf.admin.manager.activity.actoct2017.acttender;

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
public class TenderRewardActivityDefine extends BaseDefine {

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/activity/actten/tenderreward";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "manager/activity/act2017/actten/tenderReward";
    
    /** 列表画面 路径 */
    public static final String LIST_PATH_DETAIL = "manager/activity/act2017/actten/tenderRewardDetail";
    
    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    
    /** 列表画面 @RequestMapping值 */
    public static final String INIT_DETAIL = "initDetail";
    
    /** 检索数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";
    
    /** 检索数据 @RequestMapping值 */
    public static final String SEARCH_ACTION_DETAIL = "searchActionDetail";
    
	/** 数据的 @RequestMapping值 */
    public static final String EXPORT_ACTION = "exportAction";
    
    /** FROM */
    public static final String ACTTEN_TENDER_REWARD_FORM = "tenderRewardListForm";

    /** 查看权限 */
    public static final String PERMISSIONS = "activitylist";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 查找权限 */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /** 导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
