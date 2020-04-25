package com.hyjf.activity.actten.act2;

import com.hyjf.base.bean.BaseBean;

public class ActQuestionTenRequestBean extends BaseBean {

    // 哪个问题
    private String questionId;

    // 答案是什么
    private String answer;

    // 答题人
    private String userName;
    
    // 发送状态
    private Boolean sendFlg;
    
    // 手机号
    private String mobile;
    
    // 中的优惠券
    private String coupon;
    
    // 真实姓名
    private String trueName;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getSendFlg() {
        return sendFlg;
    }

    public void setSendFlg(Boolean sendFlg) {
        this.sendFlg = sendFlg;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }
}
