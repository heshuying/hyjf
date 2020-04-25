package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ActivityInviteSeven implements Serializable {
    private Integer id;

    private Integer userid;

    private Integer useridInvited;

    private String username;

    private String userRealname;

    private String usernameInvited;

    private String mobile;

    private String mobileInvited;

    private Integer registTime;

    private Integer inviteCount;

    private BigDecimal moneyFirst;

    private Integer investTime;

    private String couponName;

    private Integer rewardType;

    private String couponCode;

    private Integer sendFlg;

    private Integer addTime;

    private Integer updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getUseridInvited() {
        return useridInvited;
    }

    public void setUseridInvited(Integer useridInvited) {
        this.useridInvited = useridInvited;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getUserRealname() {
        return userRealname;
    }

    public void setUserRealname(String userRealname) {
        this.userRealname = userRealname == null ? null : userRealname.trim();
    }

    public String getUsernameInvited() {
        return usernameInvited;
    }

    public void setUsernameInvited(String usernameInvited) {
        this.usernameInvited = usernameInvited == null ? null : usernameInvited.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getMobileInvited() {
        return mobileInvited;
    }

    public void setMobileInvited(String mobileInvited) {
        this.mobileInvited = mobileInvited == null ? null : mobileInvited.trim();
    }

    public Integer getRegistTime() {
        return registTime;
    }

    public void setRegistTime(Integer registTime) {
        this.registTime = registTime;
    }

    public Integer getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(Integer inviteCount) {
        this.inviteCount = inviteCount;
    }

    public BigDecimal getMoneyFirst() {
        return moneyFirst;
    }

    public void setMoneyFirst(BigDecimal moneyFirst) {
        this.moneyFirst = moneyFirst;
    }

    public Integer getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Integer investTime) {
        this.investTime = investTime;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName == null ? null : couponName.trim();
    }

    public Integer getRewardType() {
        return rewardType;
    }

    public void setRewardType(Integer rewardType) {
        this.rewardType = rewardType;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode == null ? null : couponCode.trim();
    }

    public Integer getSendFlg() {
        return sendFlg;
    }

    public void setSendFlg(Integer sendFlg) {
        this.sendFlg = sendFlg;
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
}