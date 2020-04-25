package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;

public class PreRegistChannelExclusiveActivity implements Serializable {
    private Integer id;

    private Integer userId;

    private String username;

    private String mobile;

    private Integer referrer;

    private String referrerUserName;

    private Integer preRegistTime;

    private Integer registTime;

    private Integer utmId;

    private String utmTerm;

    private Integer sourceId;

    private String utmSource;

    private BigDecimal tenderTotal;

    private BigDecimal tenderSingle;

    private String reward;

    private String remark;

    private Integer createTime;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Integer getReferrer() {
        return referrer;
    }

    public void setReferrer(Integer referrer) {
        this.referrer = referrer;
    }

    public String getReferrerUserName() {
        return referrerUserName;
    }

    public void setReferrerUserName(String referrerUserName) {
        this.referrerUserName = referrerUserName == null ? null : referrerUserName.trim();
    }

    public Integer getPreRegistTime() {
        return preRegistTime;
    }

    public void setPreRegistTime(Integer preRegistTime) {
        this.preRegistTime = preRegistTime;
    }

    public Integer getRegistTime() {
        return registTime;
    }

    public void setRegistTime(Integer registTime) {
        this.registTime = registTime;
    }

    public Integer getUtmId() {
        return utmId;
    }

    public void setUtmId(Integer utmId) {
        this.utmId = utmId;
    }

    public String getUtmTerm() {
        return utmTerm;
    }

    public void setUtmTerm(String utmTerm) {
        this.utmTerm = utmTerm == null ? null : utmTerm.trim();
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource == null ? null : utmSource.trim();
    }

    public BigDecimal getTenderTotal() {
        return tenderTotal;
    }

    public void setTenderTotal(BigDecimal tenderTotal) {
        this.tenderTotal = tenderTotal;
    }

    public BigDecimal getTenderSingle() {
        return tenderSingle;
    }

    public void setTenderSingle(BigDecimal tenderSingle) {
        this.tenderSingle = tenderSingle;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward == null ? null : reward.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }
}