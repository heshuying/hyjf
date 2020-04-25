package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;

public class BorrowRepay implements Serializable {
    private Integer id;

    private Integer status;

    private Integer userId;

    private String borrowNid;

    private String nid;

    private String repayType;

    private BigDecimal repayFee;

    private String repayDays;

    private Integer repayStep;

    private String repayActionTime;

    private Integer repayStatus;

    private Integer repayPeriod;

    private String repayTime;

    private String repayYestime;

    private BigDecimal repayAccountAll;

    private BigDecimal repayAccount;

    private BigDecimal repayInterest;

    private BigDecimal repayCapital;

    private BigDecimal repayAccountYes;

    private Integer lateRepayDays;

    private BigDecimal repayInterestYes;

    private BigDecimal repayCapitalYes;

    private BigDecimal repayCapitalWait;

    private BigDecimal repayInterestWait;

    private Integer repayWeb;

    private String repayWebTime;

    private Integer repayWebStep;

    private BigDecimal repayWebAccount;

    private Integer repayVouch;

    private Integer advanceStatus;

    private Integer chargeDays;

    private BigDecimal chargeInterest;

    private Integer lateRepayStatus;

    private Integer lateDays;

    private BigDecimal lateInterest;

    private BigDecimal lateForfeit;

    private BigDecimal lateReminder;

    private Integer delayDays;

    private BigDecimal delayInterest;

    private String delayRemark;

    private String addtime;

    private String addip;

    private Integer createTime;

    private Integer repayMoneySource;

    private Integer repayUserId;

    private String repayUsername;

    private Integer repaySmsReminder;

    private Integer autoRepay;

    private BigDecimal orgChargeInterest;

    private BigDecimal orgLateInterest;

    private BigDecimal orgDelayInterest;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getBorrowNid() {
        return borrowNid;
    }

    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid == null ? null : borrowNid.trim();
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid == null ? null : nid.trim();
    }

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType == null ? null : repayType.trim();
    }

    public BigDecimal getRepayFee() {
        return repayFee;
    }

    public void setRepayFee(BigDecimal repayFee) {
        this.repayFee = repayFee;
    }

    public String getRepayDays() {
        return repayDays;
    }

    public void setRepayDays(String repayDays) {
        this.repayDays = repayDays == null ? null : repayDays.trim();
    }

    public Integer getRepayStep() {
        return repayStep;
    }

    public void setRepayStep(Integer repayStep) {
        this.repayStep = repayStep;
    }

    public String getRepayActionTime() {
        return repayActionTime;
    }

    public void setRepayActionTime(String repayActionTime) {
        this.repayActionTime = repayActionTime == null ? null : repayActionTime.trim();
    }

    public Integer getRepayStatus() {
        return repayStatus;
    }

    public void setRepayStatus(Integer repayStatus) {
        this.repayStatus = repayStatus;
    }

    public Integer getRepayPeriod() {
        return repayPeriod;
    }

    public void setRepayPeriod(Integer repayPeriod) {
        this.repayPeriod = repayPeriod;
    }

    public String getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(String repayTime) {
        this.repayTime = repayTime == null ? null : repayTime.trim();
    }

    public String getRepayYestime() {
        return repayYestime;
    }

    public void setRepayYestime(String repayYestime) {
        this.repayYestime = repayYestime == null ? null : repayYestime.trim();
    }

    public BigDecimal getRepayAccountAll() {
        return repayAccountAll;
    }

    public void setRepayAccountAll(BigDecimal repayAccountAll) {
        this.repayAccountAll = repayAccountAll;
    }

    public BigDecimal getRepayAccount() {
        return repayAccount;
    }

    public void setRepayAccount(BigDecimal repayAccount) {
        this.repayAccount = repayAccount;
    }

    public BigDecimal getRepayInterest() {
        return repayInterest;
    }

    public void setRepayInterest(BigDecimal repayInterest) {
        this.repayInterest = repayInterest;
    }

    public BigDecimal getRepayCapital() {
        return repayCapital;
    }

    public void setRepayCapital(BigDecimal repayCapital) {
        this.repayCapital = repayCapital;
    }

    public BigDecimal getRepayAccountYes() {
        return repayAccountYes;
    }

    public void setRepayAccountYes(BigDecimal repayAccountYes) {
        this.repayAccountYes = repayAccountYes;
    }

    public Integer getLateRepayDays() {
        return lateRepayDays;
    }

    public void setLateRepayDays(Integer lateRepayDays) {
        this.lateRepayDays = lateRepayDays;
    }

    public BigDecimal getRepayInterestYes() {
        return repayInterestYes;
    }

    public void setRepayInterestYes(BigDecimal repayInterestYes) {
        this.repayInterestYes = repayInterestYes;
    }

    public BigDecimal getRepayCapitalYes() {
        return repayCapitalYes;
    }

    public void setRepayCapitalYes(BigDecimal repayCapitalYes) {
        this.repayCapitalYes = repayCapitalYes;
    }

    public BigDecimal getRepayCapitalWait() {
        return repayCapitalWait;
    }

    public void setRepayCapitalWait(BigDecimal repayCapitalWait) {
        this.repayCapitalWait = repayCapitalWait;
    }

    public BigDecimal getRepayInterestWait() {
        return repayInterestWait;
    }

    public void setRepayInterestWait(BigDecimal repayInterestWait) {
        this.repayInterestWait = repayInterestWait;
    }

    public Integer getRepayWeb() {
        return repayWeb;
    }

    public void setRepayWeb(Integer repayWeb) {
        this.repayWeb = repayWeb;
    }

    public String getRepayWebTime() {
        return repayWebTime;
    }

    public void setRepayWebTime(String repayWebTime) {
        this.repayWebTime = repayWebTime == null ? null : repayWebTime.trim();
    }

    public Integer getRepayWebStep() {
        return repayWebStep;
    }

    public void setRepayWebStep(Integer repayWebStep) {
        this.repayWebStep = repayWebStep;
    }

    public BigDecimal getRepayWebAccount() {
        return repayWebAccount;
    }

    public void setRepayWebAccount(BigDecimal repayWebAccount) {
        this.repayWebAccount = repayWebAccount;
    }

    public Integer getRepayVouch() {
        return repayVouch;
    }

    public void setRepayVouch(Integer repayVouch) {
        this.repayVouch = repayVouch;
    }

    public Integer getAdvanceStatus() {
        return advanceStatus;
    }

    public void setAdvanceStatus(Integer advanceStatus) {
        this.advanceStatus = advanceStatus;
    }

    public Integer getChargeDays() {
        return chargeDays;
    }

    public void setChargeDays(Integer chargeDays) {
        this.chargeDays = chargeDays;
    }

    public BigDecimal getChargeInterest() {
        return chargeInterest;
    }

    public void setChargeInterest(BigDecimal chargeInterest) {
        this.chargeInterest = chargeInterest;
    }

    public Integer getLateRepayStatus() {
        return lateRepayStatus;
    }

    public void setLateRepayStatus(Integer lateRepayStatus) {
        this.lateRepayStatus = lateRepayStatus;
    }

    public Integer getLateDays() {
        return lateDays;
    }

    public void setLateDays(Integer lateDays) {
        this.lateDays = lateDays;
    }

    public BigDecimal getLateInterest() {
        return lateInterest;
    }

    public void setLateInterest(BigDecimal lateInterest) {
        this.lateInterest = lateInterest;
    }

    public BigDecimal getLateForfeit() {
        return lateForfeit;
    }

    public void setLateForfeit(BigDecimal lateForfeit) {
        this.lateForfeit = lateForfeit;
    }

    public BigDecimal getLateReminder() {
        return lateReminder;
    }

    public void setLateReminder(BigDecimal lateReminder) {
        this.lateReminder = lateReminder;
    }

    public Integer getDelayDays() {
        return delayDays;
    }

    public void setDelayDays(Integer delayDays) {
        this.delayDays = delayDays;
    }

    public BigDecimal getDelayInterest() {
        return delayInterest;
    }

    public void setDelayInterest(BigDecimal delayInterest) {
        this.delayInterest = delayInterest;
    }

    public String getDelayRemark() {
        return delayRemark;
    }

    public void setDelayRemark(String delayRemark) {
        this.delayRemark = delayRemark == null ? null : delayRemark.trim();
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime == null ? null : addtime.trim();
    }

    public String getAddip() {
        return addip;
    }

    public void setAddip(String addip) {
        this.addip = addip == null ? null : addip.trim();
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getRepayMoneySource() {
        return repayMoneySource;
    }

    public void setRepayMoneySource(Integer repayMoneySource) {
        this.repayMoneySource = repayMoneySource;
    }

    public Integer getRepayUserId() {
        return repayUserId;
    }

    public void setRepayUserId(Integer repayUserId) {
        this.repayUserId = repayUserId;
    }

    public String getRepayUsername() {
        return repayUsername;
    }

    public void setRepayUsername(String repayUsername) {
        this.repayUsername = repayUsername == null ? null : repayUsername.trim();
    }

    public Integer getRepaySmsReminder() {
        return repaySmsReminder;
    }

    public void setRepaySmsReminder(Integer repaySmsReminder) {
        this.repaySmsReminder = repaySmsReminder;
    }

    public Integer getAutoRepay() {
        return autoRepay;
    }

    public void setAutoRepay(Integer autoRepay) {
        this.autoRepay = autoRepay;
    }

    public BigDecimal getOrgChargeInterest() {
        return orgChargeInterest;
    }

    public void setOrgChargeInterest(BigDecimal orgChargeInterest) {
        this.orgChargeInterest = orgChargeInterest;
    }

    public BigDecimal getOrgLateInterest() {
        return orgLateInterest;
    }

    public void setOrgLateInterest(BigDecimal orgLateInterest) {
        this.orgLateInterest = orgLateInterest;
    }

    public BigDecimal getOrgDelayInterest() {
        return orgDelayInterest;
    }

    public void setOrgDelayInterest(BigDecimal orgDelayInterest) {
        this.orgDelayInterest = orgDelayInterest;
    }
}