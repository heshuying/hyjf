package com.hyjf.admin.manager.activity.newyear2016;

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
public class NewyearDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/newyear2016";

	/** 用户奖品列表请求路径 */
	public static final String USER_PRIZE_LIST_INIT = "userPrizeListInit";
	/** 检索数据 @RequestMapping值 */
    public static final String SEARCH_USER_PRIZE_ACTION = "searchUserPrizeAction";
	/** 导出数据 @RequestMapping值 */
	public static final String EXPORT_USER_PRIZE_ACTION = "exportUserPrizeAction";
	/** 用户奖品列表画面 路径 */
	public static final String USER_PRIZE_LIST_PATH = "manager/activity/newyear2016/userprizelist";

	
	/** 财神活动列表请求路径 */
	public static final String CAI_SHEN_LIST_INIT = "caiShenListInit";
	/** 财神活动检索数据 @RequestMapping值 */
    public static final String SEARCH_CAI_SHEN_ACTION = "searchCaiShenAction";
    /** 导出数据 @RequestMapping值 */
	public static final String EXPORT_CAI_SHEN_ACTION = "exportCaiShenAction";
	/** 财神活动列表画面 路径 */
	public static final String CAI_SHEN_LIST_PATH = "manager/activity/newyear2016/usercardlist";

	/** 闹元宵活动列表请求路径 */
	public static final String YUAN_XIAO_LIST_INIT = "yuanXiaoListInit";
	/** 闹元宵活动检索数据 @RequestMapping值 */
    public static final String SEARCH_YUAN_XIAO_ACTION = "searchYuanXiaoAction";
    /** 闹元宵活动导出数据 @RequestMapping值 */
	public static final String EXPORT_YUAN_XIAO_ACTION = "exportYuanXiaoAction";
	/** 闹元宵活动列表画面 路径 */
	public static final String YUAN_XIAO_LIST_PATH = "manager/activity/newyear2016/useryuanxiaolist";
    
	/** 导出数据 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";
	
	/** 导出数据 @RequestMapping值 */
    public static final String EXPORT_OPPORTUNITY_ACTION = "exportOpportunityAction";
	

    
    /** 检索数据 @RequestMapping值 */
    public static final String SEARCH_OPPORTUNITY_ACTION = "searchOpportunityAction";
	
	/** FROM */
	public static final String USER_CARD_LIST_FORM = "userCardListForm";
	
	/** FROM */
	public static final String USER_YUAN_XIAO_LIST_FORM = "userYuanXiaoListForm";
	
	/** FROM */
	public static final String USER_PRIZE_LIST_FORM = "userPrizeListForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "activitylist";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

}
