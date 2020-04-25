package com.hyjf.web.user.repay;

import java.io.Serializable;
import java.util.List;

public class UserRepayProjectBean implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -2142812516666739413L;

    // 是否是汇添金项目
    public String tType;

    // 还款人id
    public String userId;
    
    //角色
    public String roleId;

    // 项目编号
    public String borrowNid;

    // 项目名称
    public String borrowName;

    // 项目还款方式
    public String borrowStyle;

    // 项目还款本金
    public String borrowCapital;

    // 项目还款本息和
    public String borrowAccount;

    // 项目还款总额 加管理费
    public String borrowTotal;

    // 当前还款期数
    public String borrowPeriod;

    // 当前服务费
    public String borrowFee;

    // 当前的利息
    public String borrowInterest;

    // 当前还款状态(是否完成 0未还款1还款中)
    public String borrowStatus;

    // 当前还款方式（0正常还款1提前还款2延期还款3逾期还款）
    public String advanceStatus;

    // 提前天数
    public String chargeDays;

    public String advanceDays;

    // 提前还款利息
    public String chargeInterest;

    public String advanceInterest;

    // 延期天数
    public String delayDays;

    // 延期利息
    public String delayInterest;

    // 逾期天数
    public String lateDays;

    // 逾期罚息
    public String lateInterest;
    
    public String username;

    /** 用户还款详情 */
    private List<UserRepayBean> userRepayList;

    public UserRepayProjectBean() {
        super();
    }

    public String getBorrowNid() {
        return borrowNid;
    }

    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }

    public String getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }

    public String getBorrowStyle() {
        return borrowStyle;
    }

    public void setBorrowStyle(String borrowStyle) {
        this.borrowStyle = borrowStyle;
    }

    public String getBorrowTotal() {
        return borrowTotal;
    }

    public void setBorrowTotal(String borrowTotal) {
        this.borrowTotal = borrowTotal;
    }

    public List<UserRepayBean> getUserRepayList() {
        return userRepayList;
    }

    public void setUserRepayList(List<UserRepayBean> userRepayList) {
        this.userRepayList = userRepayList;
    }

    public String getBorrowPeriod() {
        return borrowPeriod;
    }

    public void setBorrowPeriod(String borrowPeriod) {
        this.borrowPeriod = borrowPeriod;
    }

    public String getBorrowAccount() {
        return borrowAccount;
    }

    public void setBorrowAccount(String borrowAccount) {
        this.borrowAccount = borrowAccount;
    }

    public String getBorrowCapital() {
        return borrowCapital;
    }

    public void setBorrowCapital(String borrowCapital) {
        this.borrowCapital = borrowCapital;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBorrowFee() {
        return borrowFee;
    }

    public void setBorrowFee(String borrowFee) {
        this.borrowFee = borrowFee;
    }

    public String getBorrowInterest() {
        return borrowInterest;
    }

    public void setBorrowInterest(String borrowInterest) {
        this.borrowInterest = borrowInterest;
    }

    public String getBorrowStatus() {
        return borrowStatus;
    }

    public void setBorrowStatus(String borrowStatus) {
        this.borrowStatus = borrowStatus;
    }

    public String getAdvanceStatus() {
        return advanceStatus;
    }

    public void setAdvanceStatus(String advanceStatus) {
        this.advanceStatus = advanceStatus;
    }

    public String getChargeDays() {
        return chargeDays;
    }

    public void setChargeDays(String chargeDays) {
        this.chargeDays = chargeDays;
    }

    public String getChargeInterest() {
        return chargeInterest;
    }

    public void setChargeInterest(String chargeInterest) {
        this.chargeInterest = chargeInterest;
    }

    public String getDelayDays() {
        return delayDays;
    }

    public void setDelayDays(String delayDays) {
        this.delayDays = delayDays;
    }

    public String getDelayInterest() {
        return delayInterest;
    }

    public void setDelayInterest(String delayInterest) {
        this.delayInterest = delayInterest;
    }

    public String getLateDays() {
        return lateDays;
    }

    public void setLateDays(String lateDays) {
        this.lateDays = lateDays;
    }

    public String getLateInterest() {
        return lateInterest;
    }

    public void setLateInterest(String lateInterest) {
        this.lateInterest = lateInterest;
    }

    public String gettType() {
        return tType;
    }

    public void settType(String tType) {
        this.tType = tType;
    }

    public String getAdvanceDays() {
        return advanceDays;
    }

    public void setAdvanceDays(String advanceDays) {
        this.advanceDays = advanceDays;
    }

    public String getAdvanceInterest() {
        return advanceInterest;
    }

    public void setAdvanceInterest(String advanceInterest) {
        this.advanceInterest = advanceInterest;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}
