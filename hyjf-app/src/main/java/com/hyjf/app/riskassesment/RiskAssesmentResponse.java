package com.hyjf.app.riskassesment;

import java.util.List;

import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.mybatis.model.customize.app.NewAppQuestionCustomize;

/**
 * @author xiasq
 * @version RiskAssesmentResponse, v0.1 2017/12/3 11:01
 */
public class RiskAssesmentResponse extends BaseResultBeanFrontEnd {

    private String couponResult;

    private List<NewAppQuestionCustomize> questionList;

    //测评状态
    private String resultStatus; // 0:未测评  1：已测评

    //测评类型
    private String resultType;

    //类型文字描述
    private String resultText;

    //优惠券发放结果
    private String sendResult;

    //测评上限金额
    private String revaluationMoney;

    public String getRevaluationMoney() {
        return revaluationMoney;
    }

    public void setRevaluationMoney(String revaluationMoney) {
        this.revaluationMoney = revaluationMoney;
    }

    public String getCouponResult() {
        return couponResult;
    }

    public void setCouponResult(String couponResult) {
        this.couponResult = couponResult;
    }

    public List<NewAppQuestionCustomize> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<NewAppQuestionCustomize> questionList) {
        this.questionList = questionList;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    public String getSendResult() {
        return sendResult;
    }

    public void setSendResult(String sendResult) {
        this.sendResult = sendResult;
    }
}
