/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author PC-LIUSHOUYI
 * @version HjhBailConfigCustomize, v0.1 2018/7/24 14:53
 */
public class HjhBailConfigCustomize implements Serializable {

    private Integer id;

    private String instCode;

    private String instName;

    private BigDecimal bailTatol;

    private Integer bailRate;

    private String timestart;

    private String timeend;

    private BigDecimal newCreditLine;

    private BigDecimal loanCreditLine;

    private BigDecimal dayMarkLine;

    private BigDecimal monthMarkLine;

    private BigDecimal pushMarkLine;

    private BigDecimal loanMarkLine;

    private BigDecimal remainMarkLine;

    private BigDecimal repayedCapital;

    private BigDecimal hisLoanTotal;

    private BigDecimal cycLoanTotal;

    private BigDecimal loanBalance;

    private Integer repayCapitalType;

    private Integer isAccumulate;

    private Integer isNewCredit;

    private Integer isLoanCredit;

    private String remark;

    private Integer createUserId;

    private Integer updateUserId;

    private Date createTime;

    private Date updateTime;

    private Integer delFlg;

    private int limitStart = -1;

    private int limitEnd = -1;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInstCode() {
        return instCode;
    }

    public void setInstCode(String instCode) {
        this.instCode = instCode == null ? null : instCode.trim();
    }

    public BigDecimal getBailTatol() {
        return bailTatol;
    }

    public void setBailTatol(BigDecimal bailTatol) {
        this.bailTatol = bailTatol;
    }

    public Integer getBailRate() {
        return bailRate;
    }

    public void setBailRate(Integer bailRate) {
        this.bailRate = bailRate;
    }

    public BigDecimal getNewCreditLine() {
        return newCreditLine;
    }

    public void setNewCreditLine(BigDecimal newCreditLine) {
        this.newCreditLine = newCreditLine;
    }

    public BigDecimal getLoanCreditLine() {
        return loanCreditLine;
    }

    public void setLoanCreditLine(BigDecimal loanCreditLine) {
        this.loanCreditLine = loanCreditLine;
    }

    public BigDecimal getDayMarkLine() {
        return dayMarkLine;
    }

    public void setDayMarkLine(BigDecimal dayMarkLine) {
        this.dayMarkLine = dayMarkLine;
    }

    public BigDecimal getMonthMarkLine() {
        return monthMarkLine;
    }

    public void setMonthMarkLine(BigDecimal monthMarkLine) {
        this.monthMarkLine = monthMarkLine;
    }

    public BigDecimal getPushMarkLine() {
        return pushMarkLine;
    }

    public void setPushMarkLine(BigDecimal pushMarkLine) {
        this.pushMarkLine = pushMarkLine;
    }

    public BigDecimal getLoanMarkLine() {
        return loanMarkLine;
    }

    public void setLoanMarkLine(BigDecimal loanMarkLine) {
        this.loanMarkLine = loanMarkLine;
    }

    public BigDecimal getRemainMarkLine() {
        return remainMarkLine;
    }

    public void setRemainMarkLine(BigDecimal remainMarkLine) {
        this.remainMarkLine = remainMarkLine;
    }

    public BigDecimal getRepayedCapital() {
        return repayedCapital;
    }

    public void setRepayedCapital(BigDecimal repayedCapital) {
        this.repayedCapital = repayedCapital;
    }

    public BigDecimal getHisLoanTotal() {
        return hisLoanTotal;
    }

    public void setHisLoanTotal(BigDecimal hisLoanTotal) {
        this.hisLoanTotal = hisLoanTotal;
    }

    public BigDecimal getCycLoanTotal() {
        return cycLoanTotal;
    }

    public void setCycLoanTotal(BigDecimal cycLoanTotal) {
        this.cycLoanTotal = cycLoanTotal;
    }

    public BigDecimal getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(BigDecimal loanBalance) {
        this.loanBalance = loanBalance;
    }

    public Integer getRepayCapitalType() {
        return repayCapitalType;
    }

    public void setRepayCapitalType(Integer repayCapitalType) {
        this.repayCapitalType = repayCapitalType;
    }

    public Integer getIsAccumulate() {
        return isAccumulate;
    }

    public void setIsAccumulate(Integer isAccumulate) {
        this.isAccumulate = isAccumulate;
    }

    public Integer getIsNewCredit() {
        return isNewCredit;
    }

    public void setIsNewCredit(Integer isNewCredit) {
        this.isNewCredit = isNewCredit;
    }

    public Integer getIsLoanCredit() {
        return isLoanCredit;
    }

    public void setIsLoanCredit(Integer isLoanCredit) {
        this.isLoanCredit = isLoanCredit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }

    public String getInstName() {
        return instName;
    }

    public void setInstName(String instName) {
        this.instName = instName;
    }

    public int getLimitStart() {
        return limitStart;
    }

    public void setLimitStart(int limitStart) {
        this.limitStart = limitStart;
    }

    public int getLimitEnd() {
        return limitEnd;
    }

    public void setLimitEnd(int limitEnd) {
        this.limitEnd = limitEnd;
    }

    public String getTimestart() {
        return timestart;
    }

    public void setTimestart(String timestart) {
        this.timestart = timestart;
    }

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }
}
