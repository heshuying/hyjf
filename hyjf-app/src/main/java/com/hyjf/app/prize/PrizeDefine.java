package com.hyjf.app.prize;

import com.hyjf.app.BaseDefine;

public class PrizeDefine  extends BaseDefine {
	/** 统计类名 */
	public static final String THIS_CLASS = PrizeDefine.class.getName();
	
	/** 用户登录请求 */
    public static final String LOGIN_REQUEST_MAPPING = "hyjf://jumpLogin/?";

	/** REQUEST_MAPPING */
	public static final String REQUEST_MAPPING = "/prize";

	/** 夺宝初始  */
	public static final String INIT_ACTION= "/init";
	/** 夺宝初始(微官网)  */
	public static final String INIT_WEI_ACTION= "/initWei";
	/** 参与夺宝  */
	public static final String USER_PRIZE_ACTION= "/userPrize";
	/** 参与夺宝(微官网)  */
	public static final String USER_PRIZE_WEI_ACTION= "/userPrizeWei";
	/** 我的兑奖码  */
	public static final String USER_PRIZE_LIST_ACTION= "/userPrizeList";
	/** 我的兑奖码(微官网)  */
	public static final String USER_PRIZE_LIST_WEI_ACTION= "/userPrizeListWei";
	
	/** 夺宝页面  */
	public static final String PRIZE_PATH= "act/prize/prize";
	
	public static final Integer TENDER_ACCOUNT_SCD = 1000;
	/** errorcode */
    public static final String ERROR_CODE = "errorCode";
	
    /** @RequestMapping值 */
    public static final String RETURN_REQUEST= REQUEST_HOME + REQUEST_MAPPING + USER_PRIZE_LIST_ACTION;     
    
    public static final String RETURN_REQUEST_INIT= REQUEST_HOME + REQUEST_MAPPING + INIT_ACTION;   
	
}
