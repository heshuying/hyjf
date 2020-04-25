package com.hyjf.app.activity.newyear;

import com.hyjf.app.BaseDefine;

/**
 * 
 * 
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月25日
 * @see 下午2:40:11
 */
public class NewYearActivityDefine extends BaseDefine {

    /***/
    public static final String REQUEST_MAPPING = "/activity/newyearactivity";
    /** 页面初始化 */
    public static final String INIT_ACTION = "/init.do";
    
    /** 活动二校验 */
    public static final String CHECK_ACTION = "/check.do";
    
    /** 获取财富卡数据*/
    public static final String GET_CARD_DATA = "getCardData";
    
    /** 点燃爆竹抽奖*/
    public static final String DO_PRIZE_DRAW = "doPrizeDraw";
    
    /** 手机号码校验*/
    public static final String DO_PHONENUM_CHECK = "doPhoneNumCheck";
    
    /** 发送财富卡给好友*/
    public static final String DO_CARD_SEND = "doCardSend";
    
    /** 获取活动A的活动规则*/
    public static final String GET_RULE_ACTIVITY_A = "getRuleActivityA";
    
    public static final String PATH_RULE_ACTIVITY_A = "/act/cnNewYear/rule-1";
    
    /** 初始化记录用户答题信息 */
    public static final String INSERT_USER_ANSWER_RECORD_INIT = "/insertUserAnswerRecordInit.do";
    
    /** 修改记录用户答题信息 */
    public static final String UPDATE_USER_ANSWER_RECORD = "/updateUserAnswerRecord.do";

    public static final String INIT_PATH = "/act/cnNewYear/newYear";
}
