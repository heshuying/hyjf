package com.hyjf.mybatis.model.customize.app;

import java.io.Serializable;

public class AppUserPrizeOpportunityCustomize implements Serializable {


    private static final long serialVersionUID = 1L;
    
    private String userId;
    
    private String username;
    
    private String truename;
    
    private String mobile;
    
    private String referrerUserName;
    
    //注册时间
    private String regTimeFormat;
    //活动期内累计出借总额
    private String investTotal;
    //已使用夺宝次数
    private String prizeCountAlready;
    //剩余夺宝次数
    private String prizeCountRemain;
    //代金券是否已发
    private String isCouponSend;
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getReferrerUserName() {
        return referrerUserName;
    }

    public void setReferrerUserName(String referrerUserName) {
        this.referrerUserName = referrerUserName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRegTimeFormat() {
        return regTimeFormat;
    }

    public void setRegTimeFormat(String regTimeFormat) {
        this.regTimeFormat = regTimeFormat;
    }

    public String getInvestTotal() {
        return investTotal;
    }

    public void setInvestTotal(String investTotal) {
        this.investTotal = investTotal;
    }

    public String getPrizeCountAlready() {
        return prizeCountAlready;
    }

    public void setPrizeCountAlready(String prizeCountAlready) {
        this.prizeCountAlready = prizeCountAlready;
    }

    public String getPrizeCountRemain() {
        return prizeCountRemain;
    }

    public void setPrizeCountRemain(String prizeCountRemain) {
        this.prizeCountRemain = prizeCountRemain;
    }

    public String getIsCouponSend() {
        return isCouponSend;
    }

    public void setIsCouponSend(String isCouponSend) {
        this.isCouponSend = isCouponSend;
    }


}