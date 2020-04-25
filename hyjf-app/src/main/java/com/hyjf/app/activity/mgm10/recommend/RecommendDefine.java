package com.hyjf.app.activity.mgm10.recommend;

import com.hyjf.app.BaseDefine;

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
    
    public static final String GET_USER_RECOMMEND_STAR_PRIZE_LIST_ACTION = "getUserRecommendStarPrizeList";
    public static final String GET_USER_RECOMMEND_STAR_USED_PRIZE_LIST_ACTION = "getUserRecommendStarUsedPrizeList";
    
    
    public static final String UNLOGIN_PATH = "/act/mgm10/recommend/unlogin";
    
    public static final String LOGIN_PATH = "/act/mgm10/recommend/hd-login";
    
    public static final String LIST_PATH = "/act/mgm10/recommend/hd-start";
    
    
    /** 用户登录请求 */
    public static final String LOGIN_REQUEST_MAPPING = "/user/login/init.do";
}
