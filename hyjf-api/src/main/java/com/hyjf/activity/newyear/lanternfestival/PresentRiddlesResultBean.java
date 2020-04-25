package com.hyjf.activity.newyear.lanternfestival;

import com.hyjf.base.bean.BaseResultBean;

/**
 * 
 * 谜题信息
 * @author pcc
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年1月9日
 * @see 下午3:37:37
 */
public class PresentRiddlesResultBean extends BaseResultBean{

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;
    /**是否返回问题  0没有 1有*/
    private String ifReturnQuestion = "";
    /**谜题id*/
    private String questionId = "";
    /**谜题编号*/
    private String questionNum = "";
    /**谜题信息*/
    private String questionContent= "";
    /**谜题答案*/
    private String questionAnswer= "";
    /**谜题小贴士*/
    private String questionHint= "";
    /**谜题应答时间*/
    private String answerTime= "";
    /**谜题应答时间*/
    private String questionImageName= "";
    /**猜灯谜活动标示 0未开始  1进行中  2已结束*/
    private String lanternFestivalFlag="1";
    

    
    public String getQuestionId() {
        return questionId;
    }
    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
    public String getQuestionNum() {
        return questionNum;
    }
    public void setQuestionNum(String questionNum) {
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
    public String getQuestionHint() {
        return questionHint;
    }
    public void setQuestionHint(String questionHint) {
        this.questionHint = questionHint;
    }
    public String getAnswerTime() {
        return answerTime;
    }
    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }
    public String getQuestionImageName() {
        return questionImageName;
    }
    public void setQuestionImageName(String questionImageName) {
        this.questionImageName = questionImageName;
    }
    public String getIfReturnQuestion() {
        return ifReturnQuestion;
    }
    public void setIfReturnQuestion(String ifReturnQuestion) {
        this.ifReturnQuestion = ifReturnQuestion;
    }
    public String getLanternFestivalFlag() {
        return lanternFestivalFlag;
    }
    public void setLanternFestivalFlag(String lanternFestivalFlag) {
        this.lanternFestivalFlag = lanternFestivalFlag;
    }

    
}
