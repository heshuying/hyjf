package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class NewyearQuestionUser implements Serializable {
    private Integer id;

    private Integer questionId;

    private Integer questionNum;

    private Integer userId;

    private Integer currentExchange;

    private String viewName;

    private Integer prizeJine;

    private String userAnswer;

    private Integer userAnswerResult;

    private Integer doubleFlg;

    private Integer sendFlg;

    private String remark;

    private Integer addTime;

    private Integer delFlg;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(Integer questionNum) {
        this.questionNum = questionNum;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCurrentExchange() {
        return currentExchange;
    }

    public void setCurrentExchange(Integer currentExchange) {
        this.currentExchange = currentExchange;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName == null ? null : viewName.trim();
    }

    public Integer getPrizeJine() {
        return prizeJine;
    }

    public void setPrizeJine(Integer prizeJine) {
        this.prizeJine = prizeJine;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer == null ? null : userAnswer.trim();
    }

    public Integer getUserAnswerResult() {
        return userAnswerResult;
    }

    public void setUserAnswerResult(Integer userAnswerResult) {
        this.userAnswerResult = userAnswerResult;
    }

    public Integer getDoubleFlg() {
        return doubleFlg;
    }

    public void setDoubleFlg(Integer doubleFlg) {
        this.doubleFlg = doubleFlg;
    }

    public Integer getSendFlg() {
        return sendFlg;
    }

    public void setSendFlg(Integer sendFlg) {
        this.sendFlg = sendFlg;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getAddTime() {
        return addTime;
    }

    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }
}