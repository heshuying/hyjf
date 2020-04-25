package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ActivityReturncash implements Serializable {
    private Integer id;

    private Integer userId;

    private String userName;

    private String trueName;

    private BigDecimal withdrawMoney;

    private BigDecimal total;

    private String phoneNum;

    private Integer callSuccess;

    private String investAttitude;

    private String remark;

    private String orderId;

    private Integer status;

    private Integer islost;

    private Integer hasLostreward;

    private BigDecimal investTotal;

    private BigDecimal rewardTotal;

    private Integer addTime;

    private String addUser;

    private Integer updateTime;

    private String updateUser;

    private Integer delFlg;

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

    public BigDecimal getWithdrawMoney() {
        return withdrawMoney;
    }

    public void setWithdrawMoney(BigDecimal withdrawMoney) {
        this.withdrawMoney = withdrawMoney;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum == null ? null : phoneNum.trim();
    }

    public Integer getCallSuccess() {
        return callSuccess;
    }

    public void setCallSuccess(Integer callSuccess) {
        this.callSuccess = callSuccess;
    }

    public String getInvestAttitude() {
        return investAttitude;
    }

    public void setInvestAttitude(String investAttitude) {
        this.investAttitude = investAttitude == null ? null : investAttitude.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIslost() {
        return islost;
    }

    public void setIslost(Integer islost) {
        this.islost = islost;
    }

    public Integer getHasLostreward() {
        return hasLostreward;
    }

    public void setHasLostreward(Integer hasLostreward) {
        this.hasLostreward = hasLostreward;
    }

    public BigDecimal getInvestTotal() {
        return investTotal;
    }

    public void setInvestTotal(BigDecimal investTotal) {
        this.investTotal = investTotal;
    }

    public BigDecimal getRewardTotal() {
        return rewardTotal;
    }

    public void setRewardTotal(BigDecimal rewardTotal) {
        this.rewardTotal = rewardTotal;
    }

    public Integer getAddTime() {
        return addTime;
    }

    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    public String getAddUser() {
        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser == null ? null : addUser.trim();
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }
}