package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ActdecTenderBalloon implements Serializable {
    private Integer id;

    private Integer userId;

    private String userName;

    private String trueName;

    private String mobile;

    private String tenderNid;

    private BigDecimal tenderMoney;

    private Integer isFirstTender;

    private Integer tenderType;

    private Integer balloonCount;

    private Integer ballonCanReceive;

    private Integer ballonReceived;

    private String rewardName;

    private Integer sendStatus;

    private Integer tenderTime;

    private Integer updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName == null ? null : trueName.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getTenderNid() {
        return tenderNid;
    }

    public void setTenderNid(String tenderNid) {
        this.tenderNid = tenderNid == null ? null : tenderNid.trim();
    }

    public BigDecimal getTenderMoney() {
        return tenderMoney;
    }

    public void setTenderMoney(BigDecimal tenderMoney) {
        this.tenderMoney = tenderMoney;
    }

    public Integer getIsFirstTender() {
        return isFirstTender;
    }

    public void setIsFirstTender(Integer isFirstTender) {
        this.isFirstTender = isFirstTender;
    }

    public Integer getTenderType() {
        return tenderType;
    }

    public void setTenderType(Integer tenderType) {
        this.tenderType = tenderType;
    }

    public Integer getBalloonCount() {
        return balloonCount;
    }

    public void setBalloonCount(Integer balloonCount) {
        this.balloonCount = balloonCount;
    }

    public Integer getBallonCanReceive() {
        return ballonCanReceive;
    }

    public void setBallonCanReceive(Integer ballonCanReceive) {
        this.ballonCanReceive = ballonCanReceive;
    }

    public Integer getBallonReceived() {
        return ballonReceived;
    }

    public void setBallonReceived(Integer ballonReceived) {
        this.ballonReceived = ballonReceived;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName == null ? null : rewardName.trim();
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Integer getTenderTime() {
        return tenderTime;
    }

    public void setTenderTime(Integer tenderTime) {
        this.tenderTime = tenderTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }
}