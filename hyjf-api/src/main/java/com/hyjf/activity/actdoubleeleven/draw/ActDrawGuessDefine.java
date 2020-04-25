package com.hyjf.activity.actdoubleeleven.draw;

import com.hyjf.base.bean.BaseDefine;

public class ActDrawGuessDefine extends BaseDefine {

    /** 双十一活动 我画你猜 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/invitejan";
    /** 双十一活动 我画你猜 @RequestMapping值 */
    public static final String GET_USER_QUESTION_ACTION = "/janGetUserQuestion";
    /** 十月份活动2 答案检查 @RequestMapping值 */
    public static final String ANSWER_CHECK_ACTION = "/janAnswerCheck";
   
    /** 答题状态     未答题 */
    public static final String STATE_NOT_ANSWERED = "0";
    /** 答题状态     已答题 */
    public static final String STATE_ANSWERED = "1";
    
    
    /** 活动类型 5  */
    public static final Integer ACT_TYPE = 5;
}
