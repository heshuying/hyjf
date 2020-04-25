package com.hyjf.web.activity.newyear;

import com.hyjf.web.BaseDefine;

/**
 * 
 * 新年活动
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年1月12日
 * @see 下午12:03:00
 */
public class NewYearActDefine extends BaseDefine {

    /***/
    public static final String REQUEST_MAPPING = "/activity/newyear";

    public static final String INIT_ACTION = "init";
    
    public static final String GET_CARD_DATA = "getCardData";
    
    public static final String DO_PRIZE_DRAW = "doPrizeDraw";
    
    public static final String DO_PHONENUM_CHECK = "doPhoneNumCheck";
    
    public static final String DO_CARD_SEND = "doCardSend";
    
    public static final String INIT_PATH = "/activity/active-201701";
    
    public static final String CHECK_ACTION = "check";
    /** 初始化记录用户答题信息 */
    public static final String INSERT_USER_ANSWER_RECORD_INIT = "/insertUserAnswerRecordInit.do";
    /** 修改记录用户答题信息 */
    public static final String UPDATE_USER_ANSWER_RECORD = "/updateUserAnswerRecord.do";
    
    /** 用户登录请求 */
    public static final String LOGIN_REQUEST_MAPPING = "/user/login/init.do";
    
    public static final String RET_URL = REQUEST_MAPPING + "/" + INIT_ACTION + ".do";
}
