package com.hyjf.app.user.myasset;


import java.io.Serializable;
import java.math.BigDecimal;

public class AssetBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3941884654720915922L;

	private BigDecimal frostCapital =new BigDecimal(0) ;//投标冻结金额
	private BigDecimal waitCapital =new BigDecimal(0)  ;//待收本金
	private BigDecimal waitInterest =new BigDecimal(0) ; //待收利息
	private BigDecimal recoverCapital =new BigDecimal(0) ; //已回收的本金
	private BigDecimal recoverInterest =new BigDecimal(0) ; //已回收的利息
	private BigDecimal recoverAccount =new BigDecimal(0) ;//最近待收的金额
	private String recoverTime; //估计还款时间
	private String borrowNid; //
	private BigDecimal investTotal =new BigDecimal(0) ; //累计出借金额
	
	
	public BigDecimal getFrostCapital() {
		return frostCapital;
	}
	public void setFrostCapital(BigDecimal frostCapital) {
		this.frostCapital = frostCapital;
	}
	public BigDecimal getWaitCapital() {
		return waitCapital;
	}
	public void setWaitCapital(BigDecimal waitCapital) {
		this.waitCapital = waitCapital;
	}
	public BigDecimal getWaitInterest() {
		return waitInterest;
	}
	public void setWaitInterest(BigDecimal waitInterest) {
		this.waitInterest = waitInterest;
	}
	public BigDecimal getRecoverCapital() {
		return recoverCapital;
	}
	public void setRecoverCapital(BigDecimal recoverCapital) {
		this.recoverCapital = recoverCapital;
	}
	public BigDecimal getRecoverInterest() {
		return recoverInterest;
	}
	public void setRecoverInterest(BigDecimal recoverInterest) {
		this.recoverInterest = recoverInterest;
	}
	public BigDecimal getRecoverAccount() {
		return recoverAccount;
	}
	public void setRecoverAccount(BigDecimal recoverAccount) {
		this.recoverAccount = recoverAccount;
	}
	public String getRecoverTime() {
		return recoverTime;
	}
	public void setRecoverTime(String recoverTime) {
		this.recoverTime = recoverTime;
	}
	public String getBorrowNid() {
		return borrowNid;
	}
	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}
	public BigDecimal getInvestTotal() {
		return investTotal;
	}
	public void setInvestTotal(BigDecimal investTotal) {
		this.investTotal = investTotal;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
