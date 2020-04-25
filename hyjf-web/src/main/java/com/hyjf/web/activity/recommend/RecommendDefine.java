package com.hyjf.web.activity.recommend;

import com.hyjf.web.BaseDefine;

/**
 * 
 * 
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月25日
 * @see 下午2:40:11
 */
public class RecommendDefine extends BaseDefine {

    /***/
    public static final String REQUEST_MAPPING = "/activity/recommend";
    /** @RequestMapping值 */
    public static final String REQUEST_HOME = "/hyjf-app";
    /** 当前controller名称 */
    public static final String THIS_CLASS = RecommendController.class.getName();

    public static final String INIT_ACTION = "init";
    /**下载二维码*/
    public static final String DOWNLOAD_ACTION = "download";
    
    // 奖品兑换校验
    public static final String PRIZE_CHANGE_CHECK = "/prizeChangeCheck";
    // 奖品兑换
    public static final String DO_PRIZE_CHANGE = "/doPrizeChange";
    // 抽奖
    public static final String DO_PRIZE_DRAW = "/doPrizeDraw";
    
    public static final String INIT_PATH = "/activity/active-201610";
    
    
    /** 用户登录请求 */
    public static final String LOGIN_REQUEST_MAPPING = "/user/login/init.do";
    
    public static final String RET_URL = REQUEST_MAPPING + "/" + INIT_ACTION + ".do";
}
