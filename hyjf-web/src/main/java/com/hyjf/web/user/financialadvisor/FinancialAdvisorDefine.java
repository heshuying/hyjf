package com.hyjf.web.user.financialadvisor;
/**
 * Description:出借常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月4日 下午2:33:39
 * Modification History:
 * Modified by :
 */

import com.hyjf.web.BaseDefine;

public class FinancialAdvisorDefine  extends BaseDefine{

    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/financialAdvisor";

    
    /** 风险测评初始化 */
    public static final String FINANCIAL_ADVISOR_INIT_ACTION="/financialAdvisorInit";
    /**调查问卷结果Action */
    public static final String EVALUATION_RESULT_ACTION = "/evaluationResult";
    /**再测一次Action */
    public static final String QUESTIONNAIRE_INIT_ACTION = "/questionnaireInit";
    
    
    /** 风险测评页 */
    public static final String FINANCIAL_ADVISOR_LIST_PATH="/user/financialadvisor/consultant_list";
    /** 风险测评结果页 */
    public static final String FINANCIAL_ADVISOR_RESULT_PATH="/user/financialadvisor/consultant_result";
    
    public static final String QUESTIONNAIRE_PATH="/other2";
    
    /** ValidateForm请求返回值 */
    public static final String JSON_IF_EVALUATION_KEY = "ifEvaluation";
    /** ValidateForm请求返回值 */
    public static final String JSON_ACTIVITY_IF_AVAILABLE = "activityIfAvailable";
    /** ValidateForm请求返回值*/
    public static final String JSON_USER_EVALATION_RESULT_KEY = "userEvalationResult";
    /** ValidateForm请求返回值*/
    public static final String JSON_QUESRION_LIST_KEY = "questionList";
    /** ValidateForm请求返回值*/
    public static final String JSON_USER_LOGIN_ERROR_KEY = "userError";
    /** ValidateForm请求返回值*/
    public static final String JSON_USER_LOGIN_ERROR_VLUES = "userError";
    
    /** ValidateForm请求返回值*/
    public static final String JSON_VALID_SIGN_KEY = "sign";
}

