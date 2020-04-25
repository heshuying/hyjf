package com.hyjf.admin.manager.activity.prize;

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
public class PrizeCodeListDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/activity/prize";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/activity/prize/prizecodelist";
	
	/** 列表画面 路径 */
    public static final String LIST_OPPORTUNITY_PATH = "manager/activity/prize/prizeopportunity";
	
	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	
	/** 列表画面 @RequestMapping值 */
    public static final String INIT_OPPORTUNITY = "initOpportunity";
    
	/** 导出数据 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";
	
	/** 导出数据 @RequestMapping值 */
    public static final String EXPORT_OPPORTUNITY_ACTION = "exportOpportunityAction";
	
	/** 检索数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";
    
    /** 检索数据 @RequestMapping值 */
    public static final String SEARCH_OPPORTUNITY_ACTION = "searchOpportunityAction";
	
	/** FROM */
	public static final String PRIZECODE_FORM = "prizeCodeListForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "activitylist";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
