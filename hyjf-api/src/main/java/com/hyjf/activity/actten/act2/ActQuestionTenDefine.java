package com.hyjf.activity.actten.act2;

import com.hyjf.base.bean.BaseDefine;

public class ActQuestionTenDefine extends BaseDefine {

    /** 十月份活动2 答题活动 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/inviteten";
    /** 十月份活动2 答题活动 @RequestMapping值 */
    public static final String GET_USER_QUESTION_ACTION = "/getUserQuestion";
    /** 十月份活动2 答案检查 @RequestMapping值 */
    public static final String ANSWER_CHECK_ACTION = "/answerCheck";
   
    /** 答题状态     未答题 */
    public static final String STATE_NOT_ANSWERED = "0";
    /** 答题状态     已答题 */
    public static final String STATE_ANSWERED = "1";
    
    
    /** 活动类型  */
    public static final Integer ACT_TYPE = 2;
}
