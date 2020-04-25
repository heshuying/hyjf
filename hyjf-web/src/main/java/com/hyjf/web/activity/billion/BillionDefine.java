package com.hyjf.web.activity.billion;

import com.hyjf.web.BaseDefine;

/**
 * 
 * 
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月25日
 * @see 下午2:40:11
 */
public class BillionDefine extends BaseDefine {

    /***/
    public static final String REQUEST_MAPPING = "/activity/billion";
    /** @RequestMapping值 */
    public static final String REQUEST_HOME = "/hyjf-web";
    /** 当前controller名称 */
    public static final String THIS_CLASS = BillionController.class.getName();

    public static final String INIT_ACTION = "init";
    public static final String GET_BILLION_THIRD_ACTIVITY_LIST_ACTION = "getBillionThirdActivityList";
    public static final String USER_PARTAKE_BILLION_THIRD_ACTIVITY_ACTION = "userPartakeBillionThirdActivity";
    
    
    /**下载二维码*/
    public static final String DOWNLOAD_ACTION = "download";
    
    // 奖品兑换校验
    public static final String PRIZE_CHANGE_CHECK = "/prizeChangeCheck";
    // 奖品兑换
    public static final String DO_PRIZE_CHANGE = "/doPrizeChange";
    // 抽奖
    public static final String DO_PRIZE_DRAW = "/doPrizeDraw";
    
    public static final String INIT_PATH = "/activity/billionactivity";
    
    
    /** 用户登录请求 */
    public static final String LOGIN_REQUEST_MAPPING = "/user/login/init.do?retUrl="+REQUEST_MAPPING+"/"+INIT_ACTION+".do";
    
    /** 用户登录请求 */
    public static final String INVEST_REQUEST_MAPPING = "/project/initProjectList.do?projectType=HZT";
    
    public static final String RET_URL = REQUEST_MAPPING + "/" + INIT_ACTION + ".do";
}
