package com.hyjf.financialadvisor;

import java.util.List;

import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.mybatis.model.customize.QuestionCustomize;

/**
 * 
 * 此处为类说明
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年8月16日
 * @see 下午3:04:43
 */
public class FinancialAdvisorResultBean extends BaseResultBean {
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;
    /**用户测评标示 0未测评  1已测评*/
    private int userEvaluationResultFlag=0;
    /**用户测评结果*/
    private String userEvalationResultJson="";
    /**用户行为记录编号*/
    private String behaviorId;
    /**调查问卷问题答案列表*/
    private List<QuestionCustomize> questionList;
    /**优惠券发放数量*/
    private int sendCount;
    /**优惠券发放页面展示*/
    private String sendResult;

    public int getUserEvaluationResultFlag() {
        return userEvaluationResultFlag;
    }

    public void setUserEvaluationResultFlag(int userEvaluationResultFlag) {
        this.userEvaluationResultFlag = userEvaluationResultFlag;
    }

    public String getUserEvalationResultJson() {
        return userEvalationResultJson;
    }

    public void setUserEvalationResultJson(String userEvalationResultJson) {
        this.userEvalationResultJson = userEvalationResultJson;
    }

    public String getBehaviorId() {
        return behaviorId;
    }

    public void setBehaviorId(String behaviorId) {
        this.behaviorId = behaviorId;
    }

    public List<QuestionCustomize> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<QuestionCustomize> questionList) {
        this.questionList = questionList;
    }

    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }

    public String getSendResult() {
        return sendResult;
    }

    public void setSendResult(String sendResult) {
        this.sendResult = sendResult;
    }
    
}
