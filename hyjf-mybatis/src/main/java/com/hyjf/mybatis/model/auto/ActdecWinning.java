package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class ActdecWinning implements Serializable {
    private Integer id;

    private Integer corpsId;

    private String userId;

    private String userName;

    private String mobile;

    private String winningOpid;

    private String winningHead;

    private String winningName;

    private String corpsName;

    private String captainName;

    private Integer winningType;

    private Integer amount;

    private Integer type;

    private String createUser;

    private Integer createTime;

    private String updateUser;

    private Integer updateTime;

    private Integer delFlg;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCorpsId() {
        return corpsId;
    }

    public void setCorpsId(Integer corpsId) {
        this.corpsId = corpsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
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

    public String getWinningOpid() {
        return winningOpid;
    }

    public void setWinningOpid(String winningOpid) {
        this.winningOpid = winningOpid == null ? null : winningOpid.trim();
    }

    public String getWinningHead() {
        return winningHead;
    }

    public void setWinningHead(String winningHead) {
        this.winningHead = winningHead == null ? null : winningHead.trim();
    }

    public String getWinningName() {
        return winningName;
    }

    public void setWinningName(String winningName) {
        this.winningName = winningName == null ? null : winningName.trim();
    }

    public String getCorpsName() {
        return corpsName;
    }

    public void setCorpsName(String corpsName) {
        this.corpsName = corpsName == null ? null : corpsName.trim();
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName == null ? null : captainName.trim();
    }

    public Integer getWinningType() {
        return winningType;
    }

    public void setWinningType(Integer winningType) {
        this.winningType = winningType;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }
}