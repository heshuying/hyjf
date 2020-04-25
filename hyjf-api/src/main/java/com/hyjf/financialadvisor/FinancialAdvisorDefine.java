

package com.hyjf.financialadvisor;

import com.hyjf.base.bean.BaseDefine;

public class FinancialAdvisorDefine extends BaseDefine {

    /** 风险测评 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/financialAdvisor";
    /** 返回用户是否测评标识 @RequestMapping值*/
    public static final String GET_USEREVALATIONRESULT_BY_USERID = "/getUserEvalationResultByUserId";
    /** 调查问卷初始化 @RequestMapping值*/
    public static final String GET_QUESTION_LIST = "/getQuestionList";
    /** 用户调查问卷提交答案 @RequestMapping值*/
    public static final String USER_EVALATION_END = "/userEvalationEnd";
    /** 用户调查问卷行为记录 @RequestMapping值*/
    public static final String USER_EVALUATION_BEHAVIOR = "/userEvaluationBehavior";
    /** 用户调查问卷行为记录开始 @RequestMapping值*/
    public static final String USER_EVALUATION_BEHAVIOR_STATUS = "/userEvaluationBehaviorStatus";
    /** 初始化调查问卷 */
    public static final String INIT_ACTION = "/init";
    
    

}