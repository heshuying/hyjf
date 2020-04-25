package com.hyjf.app.activity;

import com.hyjf.app.BaseDefine;

public class ActivityListDefine extends BaseDefine {


    /** 提现规则  路径 */
    public static final String WITHDRAW_RULE_PATH = "/withdraw/withdraw";
    /** 提现成功页面  路径 */
    public static final String ERROR_PATH = "error";
    
    /** 提现成功页面  路径 */
    public static final String ACTIVITYDETAIL_OTHER_PATH = "other";
    
	/** REQUEST_MAPPING */
	public static final String REQUEST_MAPPING = "/activity";

	/** 获取活动专区列表  */
	public static final String ACTIVITYLIST_ACTION = "/activityList";
	/** 跳转活动详情页  */
    public static final String ACTIVITYDETAIL_ACTION = "/activityDetail";
	
    /** @RequestMapping值 */
    public static final String RETURN_REQUEST= REQUEST_HOME + REQUEST_MAPPING + ACTIVITYLIST_ACTION;
}
