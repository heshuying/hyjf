package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;

public class HjhPlan implements Serializable {
    private Integer id;

    private String planNid;

    private String planName;

    private Integer lockPeriod;

    private Integer isMonth;

    private BigDecimal expectApr;

    private BigDecimal minInvestment;

    private BigDecimal maxInvestment;

    private BigDecimal investmentIncrement;

    private BigDecimal availableInvestAccount;

    private BigDecimal repayWaitAll;

    private Integer planInvestStatus;

    private Integer planDisplayStatus;

    private Integer addTime;

    private String borrowStyle;

    private String couponConfig;

    private BigDecimal joinTotal;

    private BigDecimal planWaitCaptical;

    private BigDecimal planWaitInterest;

    private BigDecimal repayTotal;

    private BigDecimal planRepayInterest;

    private BigDecimal planRepayCapital;

    private Integer minInvestCounts;

    private String investLevel;

    private Integer createUser;

    private Integer createTime;

    private Integer updateUser;

    private Integer updateTime;

    private Integer delFlg;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlanNid() {
        return planNid;
    }

    public void setPlanNid(String planNid) {
        this.planNid = planNid == null ? null : planNid.trim();
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName == null ? null : planName.trim();
    }

    public Integer getLockPeriod() {
        return lockPeriod;
    }

    public void setLockPeriod(Integer lockPeriod) {
        this.lockPeriod = lockPeriod;
    }

    public Integer getIsMonth() {
        return isMonth;
    }

    public void setIsMonth(Integer isMonth) {
        this.isMonth = isMonth;
    }

    public BigDecimal getExpectApr() {
        return expectApr;
    }

    public void setExpectApr(BigDecimal expectApr) {
        this.expectApr = expectApr;
    }

    public BigDecimal getMinInvestment() {
        return minInvestment;
    }

    public void setMinInvestment(BigDecimal minInvestment) {
        this.minInvestment = minInvestment;
    }

    public BigDecimal getMaxInvestment() {
        return maxInvestment;
    }

    public void setMaxInvestment(BigDecimal maxInvestment) {
        this.maxInvestment = maxInvestment;
    }

    public BigDecimal getInvestmentIncrement() {
        return investmentIncrement;
    }

    public void setInvestmentIncrement(BigDecimal investmentIncrement) {
        this.investmentIncrement = investmentIncrement;
    }

    public BigDecimal getAvailableInvestAccount() {
        return availableInvestAccount;
    }

    public void setAvailableInvestAccount(BigDecimal availableInvestAccount) {
        this.availableInvestAccount = availableInvestAccount;
    }

    public BigDecimal getRepayWaitAll() {
        return repayWaitAll;
    }

    public void setRepayWaitAll(BigDecimal repayWaitAll) {
        this.repayWaitAll = repayWaitAll;
    }

    public Integer getPlanInvestStatus() {
        return planInvestStatus;
    }

    public void setPlanInvestStatus(Integer planInvestStatus) {
        this.planInvestStatus = planInvestStatus;
    }

    public Integer getPlanDisplayStatus() {
        return planDisplayStatus;
    }

    public void setPlanDisplayStatus(Integer planDisplayStatus) {
        this.planDisplayStatus = planDisplayStatus;
    }

    public Integer getAddTime() {
        return addTime;
    }

    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    public String getBorrowStyle() {
        return borrowStyle;
    }

    public void setBorrowStyle(String borrowStyle) {
        this.borrowStyle = borrowStyle == null ? null : borrowStyle.trim();
    }

    public String getCouponConfig() {
        return couponConfig;
    }

    public void setCouponConfig(String couponConfig) {
        this.couponConfig = couponConfig == null ? null : couponConfig.trim();
    }

    public BigDecimal getJoinTotal() {
        return joinTotal;
    }

    public void setJoinTotal(BigDecimal joinTotal) {
        this.joinTotal = joinTotal;
    }

    public BigDecimal getPlanWaitCaptical() {
        return planWaitCaptical;
    }

    public void setPlanWaitCaptical(BigDecimal planWaitCaptical) {
        this.planWaitCaptical = planWaitCaptical;
    }

    public BigDecimal getPlanWaitInterest() {
        return planWaitInterest;
    }

    public void setPlanWaitInterest(BigDecimal planWaitInterest) {
        this.planWaitInterest = planWaitInterest;
    }

    public BigDecimal getRepayTotal() {
        return repayTotal;
    }

    public void setRepayTotal(BigDecimal repayTotal) {
        this.repayTotal = repayTotal;
    }

    public BigDecimal getPlanRepayInterest() {
        return planRepayInterest;
    }

    public void setPlanRepayInterest(BigDecimal planRepayInterest) {
        this.planRepayInterest = planRepayInterest;
    }

    public BigDecimal getPlanRepayCapital() {
        return planRepayCapital;
    }

    public void setPlanRepayCapital(BigDecimal planRepayCapital) {
        this.planRepayCapital = planRepayCapital;
    }

    public Integer getMinInvestCounts() {
        return minInvestCounts;
    }

    public void setMinInvestCounts(Integer minInvestCounts) {
        this.minInvestCounts = minInvestCounts;
    }

    public String getInvestLevel() {
        return investLevel;
    }

    public void setInvestLevel(String investLevel) {
        this.investLevel = investLevel == null ? null : investLevel.trim();
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
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