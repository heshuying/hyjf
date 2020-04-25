package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class ActdecSpringList implements Serializable {
    private Integer id;

    private String userId;

    private String userMobile;

    private String userName;

    private Integer operType;

    private Integer number;

    private String reward;

    private Integer totalNumber;

    private Integer availableNumber;

    private Integer newRecharge;

    private Integer newInvestment;

    private Integer operAmount;

    private String createUser;

    private Integer createTime;

    private String updateUser;

    private Integer updateTime;

    private Boolean delFlg;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile == null ? null : userMobile.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Integer getOperType() {
        return operType;
    }

    public void setOperType(Integer operType) {
        this.operType = operType;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward == null ? null : reward.trim();
    }

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public Integer getAvailableNumber() {
        return availableNumber;
    }

    public void setAvailableNumber(Integer availableNumber) {
        this.availableNumber = availableNumber;
    }

    public Integer getNewRecharge() {
        return newRecharge;
    }

    public void setNewRecharge(Integer newRecharge) {
        this.newRecharge = newRecharge;
    }

    public Integer getNewInvestment() {
        return newInvestment;
    }

    public void setNewInvestment(Integer newInvestment) {
        this.newInvestment = newInvestment;
    }

    public Integer getOperAmount() {
        return operAmount;
    }

    public void setOperAmount(Integer operAmount) {
        this.operAmount = operAmount;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Boolean delFlg) {
        this.delFlg = delFlg;
    }
}