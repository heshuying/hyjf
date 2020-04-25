package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;

public class AdminNewUserActivityCustomize implements Serializable {


    private static final long serialVersionUID = 1L;
    
    private String userName;
    
    private String mobile;
    
    //推荐人
    private String recommendName;
    
    //渠道来源
    private String sourceName;
    
    //注册平台
    private String registPlat;
    
    //注册平台编号
    private String regEsb;
    
    //优惠券已发数量
    private String couponSendAlready;
    
    //注册时间
    private String regTime;
    
    //开户时间
    private String openTime;
    
    //出借时间
    private String investTime;
    
    //出借额
    private String account;
    
    //活动类型 0 注册送体验金 1 出借送加息券
    private String activity;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRecommendName() {
        return recommendName;
    }

    public void setRecommendName(String recommendName) {
        this.recommendName = recommendName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getRegistPlat() {
        return registPlat;
    }

    public void setRegistPlat(String registPlat) {
        this.registPlat = registPlat;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }


    public String getCouponSendAlready() {
        return couponSendAlready;
    }

    public void setCouponSendAlready(String couponSendAlready) {
        this.couponSendAlready = couponSendAlready;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getRegEsb() {
        return regEsb;
    }

    public void setRegEsb(String regEsb) {
        this.regEsb = regEsb;
    }


    


}