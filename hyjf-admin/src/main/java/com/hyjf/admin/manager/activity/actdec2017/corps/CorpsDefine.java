package com.hyjf.admin.manager.activity.actdec2017.corps;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 
 * 十二月份活动
 * @author dddzs
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月26日
 * @see 上午9:56:26
 */
public class CorpsDefine extends BaseDefine {

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/activity/actdec/corps";

    /** 列表画面 路径 */
    public static final String LIST_PATH = "manager/activity/act2017/actdec/corpsList";
    
    /** 列表画面 路径 */
    public static final String LIST_PATH_DETAIL = "manager/activity/act2017/actdec/corpsListDetail";
    
    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    
    /** 列表画面 @RequestMapping值 */
    public static final String INIT_DETAIL = "initDetail";
    
    /** 检索数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";
    /** 检索数据 @RequestMapping值 */
    public static final String HONGBAO_ACTION = "hongbaoAction";
    public static final String PINGGUO_ACTION = "pingguoAction";
    /** 检索数据 @RequestMapping值 */
    public static final String SEARCH_ACTION_DETAIL = "searchActionDetail";
    
	/** 数据的 @RequestMapping值 */
    public static final String EXPORT_ACTION = "exportAction";
    
    /** FROM */
    public static final String ACTDEC_BALLOON_FORM = "corpsListForm";

    /** 查看权限 */
    public static final String PERMISSIONS = "activitylist";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

    /** 查找权限 */
    public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

    /** 导出权限 */
    public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
