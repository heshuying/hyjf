package com.hyjf.financialadvisor;

import com.hyjf.base.bean.BaseBean;

public class FinancialAdvisorBean extends BaseBean {
    /**用户行为记录编号*/
    private String behaviorId;
    /**统计内容*/
    private String behavior;
    
    private String userAnswer;
    public String getBehaviorId() {
        return behaviorId;
    }
    public void setBehaviorId(String behaviorId) {
        this.behaviorId = behaviorId;
    }
    public String getBehavior() {
        return behavior;
    }
    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }
    public String getUserAnswer() {
        return userAnswer;
    }
    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
    
}
