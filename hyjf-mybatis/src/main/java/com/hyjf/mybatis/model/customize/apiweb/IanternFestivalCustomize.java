
package com.hyjf.mybatis.model.customize.apiweb;

import java.io.Serializable;

/**
 * @author Administrator
 */

public class IanternFestivalCustomize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8184938563326277586L;
	private Integer questionNum;
	private String questionContent;
	private String questionAnswer;
	private String questionImageName;
	private String questionHint;
	private Integer doubleFlg;
	private Integer userId;
	private Integer currentExchange;
	private String prizeJine;
	private Integer userAnswerResult;
	private String viewName;
	
	private String answerTime;
	

    public Integer getQuestionNum() {
        return questionNum;
    }
    public void setQuestionNum(Integer questionNum) {
        this.questionNum = questionNum;
    }
    public String getQuestionContent() {
        return questionContent;
    }
    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }
    public String getQuestionAnswer() {
        return questionAnswer;
    }
    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }
    public String getQuestionImageName() {
        return questionImageName;
    }
    public void setQuestionImageName(String questionImageName) {
        this.questionImageName = questionImageName;
    }
    public String getQuestionHint() {
        return questionHint;
    }
    public void setQuestionHint(String questionHint) {
        this.questionHint = questionHint;
    }
    public Integer getDoubleFlg() {
        return doubleFlg;
    }
    public void setDoubleFlg(Integer doubleFlg) {
        this.doubleFlg = doubleFlg;
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
    public String getPrizeJine() {
        return prizeJine;
    }
    public void setPrizeJine(String prizeJine) {
        this.prizeJine = prizeJine;
    }
    public Integer getUserAnswerResult() {
        return userAnswerResult;
    }
    public void setUserAnswerResult(Integer userAnswerResult) {
        this.userAnswerResult = userAnswerResult;
    }
    public String getViewName() {
        return viewName;
    }
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
    public String getAnswerTime() {
        return answerTime;
    }
    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }

}
