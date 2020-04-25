package com.hyjf.api.surong.borrow.repay.info;

import java.math.BigDecimal;

import com.hyjf.base.bean.BaseResultBean;


public class BorrowRepayInfoResultBean extends BaseResultBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3988869679360594579L;

	/**
	 * 还款总期数
	 */
	Integer borrowPeriod;
	
	/**
	 * 当前还款期数
	 */
	Integer repayPeriod;
	
	/**
	 * 还款总额
	 */
	BigDecimal repayAccountAll;
	
	/**
	 * 还款本金
	 */
	BigDecimal repayCapital;
	
	/**
	 * 还款利息
	 */
	BigDecimal repayInterest;
	
	/**
	 * 管理费
	 */
	BigDecimal repayFee;
	
	/**
	 * 提前还款减息
	 */
	BigDecimal chargeInterest;
	
	/**
	 * 提前还款天数
	 */
	Integer chargeDays;
	
	/**
	 * 逾期还款加息
	 */
	BigDecimal lateInterest;
	
	/**
	 * 逾期还款天数
	 */
	Integer lateDays;
	
	/**
	 * 延期还款加息
	 */
	BigDecimal delayInterest;
	
	/**
	 * 延期还款天数
	 */
	Integer delayDays;
	
	/**
	 * 应还款时间
	 */
	Integer repayTime;
	
	/**
	 * 用户账户余额
	 */
	BigDecimal bankBalance;

	public BigDecimal getBankBalance() {
		return bankBalance;
	}

	public void setBankBalance(BigDecimal bankBalance) {
		this.bankBalance = bankBalance;
	}

	public Integer getBorrowPeriod() {
		return borrowPeriod;
	}

	public void setBorrowPeriod(Integer borrowPeriod) {
		this.borrowPeriod = borrowPeriod;
	}

	public Integer getRepayPeriod() {
		return repayPeriod;
	}

	public void setRepayPeriod(Integer repayPeriod) {
		this.repayPeriod = repayPeriod;
	}

	public BigDecimal getRepayAccountAll() {
		return repayAccountAll;
	}

	public void setRepayAccountAll(BigDecimal repayAccountAll) {
		this.repayAccountAll = repayAccountAll;
	}

	public BigDecimal getRepayCapital() {
		return repayCapital;
	}

	public void setRepayCapital(BigDecimal repayCapital) {
		this.repayCapital = repayCapital;
	}

	public BigDecimal getRepayInterest() {
		return repayInterest;
	}

	public void setRepayInterest(BigDecimal repayInterest) {
		this.repayInterest = repayInterest;
	}

	public BigDecimal getRepayFee() {
		return repayFee;
	}

	public void setRepayFee(BigDecimal repayFee) {
		this.repayFee = repayFee;
	}

	public BigDecimal getChargeInterest() {
		return chargeInterest;
	}

	public void setChargeInterest(BigDecimal chargeInterest) {
		this.chargeInterest = chargeInterest;
	}

	public Integer getChargeDays() {
		return chargeDays;
	}

	public void setChargeDays(Integer chargeDays) {
		this.chargeDays = chargeDays;
	}

	public BigDecimal getLateInterest() {
		return lateInterest;
	}

	public void setLateInterest(BigDecimal lateInterest) {
		this.lateInterest = lateInterest;
	}

	public Integer getLateDays() {
		return lateDays;
	}

	public void setLateDays(Integer lateDays) {
		this.lateDays = lateDays;
	}

	public Integer getRepayTime() {
		return repayTime;
	}

	public void setRepayTime(Integer repayTime) {
		this.repayTime = repayTime;
	}

	public BigDecimal getDelayInterest() {
		return delayInterest;
	}

	public void setDelayInterest(BigDecimal delayInterest) {
		this.delayInterest = delayInterest;
	}

	public Integer getDelayDays() {
		return delayDays;
	}

	public void setDelayDays(Integer delayDays) {
		this.delayDays = delayDays;
	}
	
	
	
}
