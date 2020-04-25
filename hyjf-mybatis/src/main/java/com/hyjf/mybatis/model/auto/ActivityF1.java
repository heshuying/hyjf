package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ActivityF1 implements Serializable {
    private Integer userId;

    private String userName;

    private String mobile;

    private String realName;

    private String activityType;

    private Integer speed;

    private BigDecimal tenderAccountAll;

    private String isAppFlg;

    private String isFirstFlg;

    private BigDecimal returnAmountActivity;

    private BigDecimal returnAmountExtra;

    private BigDecimal returnAmount;

    private Integer addTime;

    private Integer updateTime;

    private String isReturnFlg;

    private Integer returnTime;

    private static final long serialVersionUID = 1L;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType == null ? null : activityType.trim();
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public BigDecimal getTenderAccountAll() {
        return tenderAccountAll;
    }

    public void setTenderAccountAll(BigDecimal tenderAccountAll) {
        this.tenderAccountAll = tenderAccountAll;
    }

    public String getIsAppFlg() {
        return isAppFlg;
    }

    public void setIsAppFlg(String isAppFlg) {
        this.isAppFlg = isAppFlg == null ? null : isAppFlg.trim();
    }

    public String getIsFirstFlg() {
        return isFirstFlg;
    }

    public void setIsFirstFlg(String isFirstFlg) {
        this.isFirstFlg = isFirstFlg == null ? null : isFirstFlg.trim();
    }

    public BigDecimal getReturnAmountActivity() {
        return returnAmountActivity;
    }

    public void setReturnAmountActivity(BigDecimal returnAmountActivity) {
        this.returnAmountActivity = returnAmountActivity;
    }

    public BigDecimal getReturnAmountExtra() {
        return returnAmountExtra;
    }

    public void setReturnAmountExtra(BigDecimal returnAmountExtra) {
        this.returnAmountExtra = returnAmountExtra;
    }

    public BigDecimal getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
    }

    public Integer getAddTime() {
        return addTime;
    }

    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsReturnFlg() {
        return isReturnFlg;
    }

    public void setIsReturnFlg(String isReturnFlg) {
        this.isReturnFlg = isReturnFlg == null ? null : isReturnFlg.trim();
    }

    public Integer getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Integer returnTime) {
        this.returnTime = returnTime;
    }
}