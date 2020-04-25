package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class NewyearSendPrize implements Serializable {
    private Integer id;

    private Integer userPrizeId;

    private String couponCode;

    private Integer couponJine;

    private Integer activityFlg;

    private Integer userId;

    private Integer sendStatus;

    private Integer addTime;

    private Integer delFlg;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserPrizeId() {
        return userPrizeId;
    }

    public void setUserPrizeId(Integer userPrizeId) {
        this.userPrizeId = userPrizeId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode == null ? null : couponCode.trim();
    }

    public Integer getCouponJine() {
        return couponJine;
    }

    public void setCouponJine(Integer couponJine) {
        this.couponJine = couponJine;
    }

    public Integer getActivityFlg() {
        return activityFlg;
    }

    public void setActivityFlg(Integer activityFlg) {
        this.activityFlg = activityFlg;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
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