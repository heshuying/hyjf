package com.hyjf.admin.manager.activity.nami;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * 
 * 兑奖码列表
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月26日
 * @see 上午9:57:02
 */
public class NaMiActivityDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/activity/nami";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/activity/nami/nami_activity";
	
	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	
    
	/** 导出数据 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";
	
	/** 检索数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "search";
    
	/** FROM */
	public static final String PRIZECODE_FORM = "naMiListForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "activitylist";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
