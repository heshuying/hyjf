package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class ActdecListedTwo implements Serializable {
    private Integer id;

    private String username;

    private String truename;

    private Integer userId;

    private String mobile;

    private Integer trade;

    private Integer investedAmount;

    private Integer acceptPrize;

    private Integer acceptTime;

    private Integer amount;

    private Integer cumulativeCharge;

    private Integer cumulativeInvest;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename == null ? null : truename.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Integer getTrade() {
        return trade;
    }

    public void setTrade(Integer trade) {
        this.trade = trade;
    }

    public Integer getInvestedAmount() {
        return investedAmount;
    }

    public void setInvestedAmount(Integer investedAmount) {
        this.investedAmount = investedAmount;
    }

    public Integer getAcceptPrize() {
        return acceptPrize;
    }

    public void setAcceptPrize(Integer acceptPrize) {
        this.acceptPrize = acceptPrize;
    }

    public Integer getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Integer acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getCumulativeCharge() {
        return cumulativeCharge;
    }

    public void setCumulativeCharge(Integer cumulativeCharge) {
        this.cumulativeCharge = cumulativeCharge;
    }

    public Integer getCumulativeInvest() {
        return cumulativeInvest;
    }

    public void setCumulativeInvest(Integer cumulativeInvest) {
        this.cumulativeInvest = cumulativeInvest;
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