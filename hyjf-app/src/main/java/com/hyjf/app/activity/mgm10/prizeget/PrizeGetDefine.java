package com.hyjf.app.activity.mgm10.prizeget;

import com.hyjf.app.BaseDefine;

/**
 * 
 * 
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月25日
 * @see 下午2:40:11
 */
public class PrizeGetDefine extends BaseDefine {

    /***/
    public static final String REQUEST_MAPPING = "/activity/prizeget";
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = PrizeGetController.class.getName();

    public static final String PRIZE_GET_INIT_ACTION = "init";
    
    public static final String PRIZE_CHANGE_CHECK = "/prizeChangeCheck";
    
    public static final String DO_PRIZE_CHANGE = "/doPrizeChange";
    
    public static final String DO_PRIZE_DRAW = "/doPrizeDraw";
    
    public static final String PRIZE_GET_PATH = "/act/mgm10/prizeget/prizeget";
    
    /** 错误页面 @Path值 */
    public static final String ERROR_PTAH = "error";
}
