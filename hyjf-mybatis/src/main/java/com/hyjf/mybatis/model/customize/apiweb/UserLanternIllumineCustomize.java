
package com.hyjf.mybatis.model.customize.apiweb;

import java.io.Serializable;

/**
 * @author Administrator
 */

public class UserLanternIllumineCustomize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8184938563326277586L;
	
	//题目序号
	private Integer questionNum;
	//应答时间
	private String answerTime;
	//用户答题是否正确0正确1错误或未答
	private Integer userAnswerResult;
    public Integer getQuestionNum() {
        return questionNum;
    }
    public void setQuestionNum(Integer questionNum) {
        this.questionNum = questionNum;
    }
    public String getAnswerTime() {
        return answerTime;
    }
    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }
    public Integer getUserAnswerResult() {
        return userAnswerResult;
    }
    public void setUserAnswerResult(Integer userAnswerResult) {
        this.userAnswerResult = userAnswerResult;
    }

    

}
