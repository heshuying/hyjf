package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.util.Date;

public class FeeConfig implements Serializable {
    private Integer id;

    private String bankCode;

    private String name;

    private String personalCredit;

    private String enterpriseCredit;

    private String quickPayment;

    private String directTakeout;

    private String directTakeoutPercent;

    private String quickTakeout;

    private String quickTakeoutPercent;

    private String normalTakeout;

    private String remark;

    private Integer createBy;

    private Date createTime;

    private Integer updateBy;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPersonalCredit() {
        return personalCredit;
    }

    public void setPersonalCredit(String personalCredit) {
        this.personalCredit = personalCredit == null ? null : personalCredit.trim();
    }

    public String getEnterpriseCredit() {
        return enterpriseCredit;
    }

    public void setEnterpriseCredit(String enterpriseCredit) {
        this.enterpriseCredit = enterpriseCredit == null ? null : enterpriseCredit.trim();
    }

    public String getQuickPayment() {
        return quickPayment;
    }

    public void setQuickPayment(String quickPayment) {
        this.quickPayment = quickPayment == null ? null : quickPayment.trim();
    }

    public String getDirectTakeout() {
        return directTakeout;
    }

    public void setDirectTakeout(String directTakeout) {
        this.directTakeout = directTakeout == null ? null : directTakeout.trim();
    }

    public String getDirectTakeoutPercent() {
        return directTakeoutPercent;
    }

    public void setDirectTakeoutPercent(String directTakeoutPercent) {
        this.directTakeoutPercent = directTakeoutPercent == null ? null : directTakeoutPercent.trim();
    }

    public String getQuickTakeout() {
        return quickTakeout;
    }

    public void setQuickTakeout(String quickTakeout) {
        this.quickTakeout = quickTakeout == null ? null : quickTakeout.trim();
    }

    public String getQuickTakeoutPercent() {
        return quickTakeoutPercent;
    }

    public void setQuickTakeoutPercent(String quickTakeoutPercent) {
        this.quickTakeoutPercent = quickTakeoutPercent == null ? null : quickTakeoutPercent.trim();
    }

    public String getNormalTakeout() {
        return normalTakeout;
    }

    public void setNormalTakeout(String normalTakeout) {
        this.normalTakeout = normalTakeout == null ? null : normalTakeout.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}