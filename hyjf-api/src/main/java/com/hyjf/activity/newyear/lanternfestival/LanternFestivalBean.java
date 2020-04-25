package com.hyjf.activity.newyear.lanternfestival;

import com.hyjf.base.bean.BaseBean;

public class LanternFestivalBean extends BaseBean{
    private Integer userId;
    
    private String userAnswer;
    
    private Integer questionId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }
    
}
