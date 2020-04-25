package com.hyjf.activity.actdoubleeleven.bargain;

import com.hyjf.base.bean.BaseDefine;

public class BargainDefine extends BaseDefine {
	
	/** iphonex预计所需砍价人数*/
	public static final Integer BARGAIN_COUNTNEED_IPHONEX = 8000;
	/** 京东E卡预计所需砍价人数*/
	public static final Integer BARGAIN_COUNTNEED_JDECARD = 500;
	/** iphone耳机预计所需砍价人数*/
	public static final Integer BARGAIN_COUNTNEED_IPHONEEAR = 200;
	/** iphone数据线预计所需砍价人数*/
	public static final Integer BARGAIN_COUNTNEED_IPHONEWIRE = 100;

    /** 双十一砍价活动 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/actnov/bargain";
    
    /** 砍价活动获取奖品列表 @RequestMapping值*/
    public static final String GET_PRIZELIST = "/getPrizeList";
    
    /** 砍价活动获取奖品详情 @RequestMapping值*/
    public static final String GET_PRIZEDETAIL = "/getPrizeDetail";
    
    /** 砍价活动砍价列表 @RequestMapping值*/
    public static final String GET_PRIZE_BARGAIN_LIST = "/getPrizeBargainList";
    
    /** 砍价活接口 @RequestMapping值*/
    public static final String DO_BARGAIN = "/doBargain";
    
    /** 砍价翻倍接口 @RequestMapping值*/
    public static final String DO_BARGAIN_DOUBLE = "/doBargainDouble";
    
    /** 奖品购买接口 @RequestMapping值*/
    public static final String DO_PRIZE_BUY = "/doPrizeBuy";
    
    /** 发送短信验证码  @RequestMapping值*/
    public static final String DO_SMSCODE = "/doSmsCode";
    /** 短信验证码类型 */
    public static final String SMSCODE_TYPE_ACT = "act";
}
