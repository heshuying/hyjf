package com.hyjf.mybatis.model.customize.apiweb.plan;

import java.io.Serializable;

public class WeChatPlanAccountCustomize implements Serializable {
	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * 计划编号
	 */
	private String debtPlanNid;

	/**
	 * 加入金额
	 */
	private String accedeAccount;

	/**
	 * 加入时间
	 */
	private String createTime;

	/**
	 * 应清算时间
	 */
	private String liquidateShouldTime;
	/**
	 * 还款时间
	 */
	private String repayTime;

	/**
	 * 此笔加入是否已经完成 0出借中 1出借完成 2还款中 3还款完成
	 */
	private String status;
	/**
	 * 应还本息
	 */
	private String repayAccount;
	/**
	 * 已还本息
	 */
	private String repayAccountYes;
	/**
	 * 待还本息
	 */
	private String repayAccountWait;
	/**
	 * 应还本金
	 */
	private String repayCapital;
	/**
	 * 已还本金
	 */
	private String repayCapitalYes;
	/**
	 * 应还利息
	 */
	private String repayInterest;
	/**
	 * 待还利息
	 */
	private String repayInterestWait;

	/**
	 * 锁定期
	 */
	private String debtLockPeriod;

	/**
	 * 预期利息
	 */
	private String expectApr;
	/**
	 * 计划状态
	 */
	private String debtPlanStatus;
	/**
	 * 实际回款总额
	 */
	private String repayAccountFact;
	/**
	 * 实际回款利息
	 */
	private String repayInterestFact;

	/**
	 * 最晚到账时间
	 */
	private String lastRepayTime;
	/**
	 * 已还利息
	 */
	private String repayInterestYes;
	
	/**
	 * 计划完成率
	 */
	private String debtPlanAccountScale;

	public String getAccedeAccount() {
		return accedeAccount;
	}

	public void setAccedeAccount(String accedeAccount) {
		this.accedeAccount = accedeAccount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDebtPlanNid() {
		return debtPlanNid;
	}

	public void setDebtPlanNid(String debtPlanNid) {
		this.debtPlanNid = debtPlanNid;
	}

	public String getRepayTime() {
		return repayTime;
	}

	public void setRepayTime(String repayTime) {
		this.repayTime = repayTime;
	}

	public String getRepayAccount() {
		return repayAccount;
	}

	public void setRepayAccount(String repayAccount) {
		this.repayAccount = repayAccount;
	}

	public String getRepayCapital() {
		return repayCapital;
	}

	public void setRepayCapital(String repayCapital) {
		this.repayCapital = repayCapital;
	}

	public String getRepayInterest() {
		return repayInterest;
	}

	public void setRepayInterest(String repayInterest) {
		this.repayInterest = repayInterest;
	}

	public String getRepayInterestWait() {
		return repayInterestWait;
	}

	public void setRepayInterestWait(String repayInterestWait) {
		this.repayInterestWait = repayInterestWait;
	}

	public String getRepayCapitalYes() {
		return repayCapitalYes;
	}

	public void setRepayCapitalYes(String repayCapitalYes) {
		this.repayCapitalYes = repayCapitalYes;
	}

	public String getRepayAccountYes() {
		return repayAccountYes;
	}

	public void setRepayAccountYes(String repayAccountYes) {
		this.repayAccountYes = repayAccountYes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRepayAccountWait() {
		return repayAccountWait;
	}

	public void setRepayAccountWait(String repayAccountWait) {
		this.repayAccountWait = repayAccountWait;
	}

	public String getLiquidateShouldTime() {
		return liquidateShouldTime;
	}

	public void setLiquidateShouldTime(String liquidateShouldTime) {
		this.liquidateShouldTime = liquidateShouldTime;
	}

	public String getDebtLockPeriod() {
		return debtLockPeriod;
	}

	public void setDebtLockPeriod(String debtLockPeriod) {
		this.debtLockPeriod = debtLockPeriod;
	}

	public String getExpectApr() {
		return expectApr;
	}

	public void setExpectApr(String expectApr) {
		this.expectApr = expectApr;
	}

	public String getDebtPlanStatus() {
		return debtPlanStatus;
	}

	public void setDebtPlanStatus(String debtPlanStatus) {
		this.debtPlanStatus = debtPlanStatus;
	}

	public String getRepayAccountFact() {
		return repayAccountFact;
	}

	public void setRepayAccountFact(String repayAccountFact) {
		this.repayAccountFact = repayAccountFact;
	}

	public String getRepayInterestFact() {
		return repayInterestFact;
	}

	public void setRepayInterestFact(String repayInterestFact) {
		this.repayInterestFact = repayInterestFact;
	}

	public String getLastRepayTime() {
		return lastRepayTime;
	}

	public void setLastRepayTime(String lastRepayTime) {
		this.lastRepayTime = lastRepayTime;
	}

	public String getRepayInterestYes() {
		return repayInterestYes;
	}

	public void setRepayInterestYes(String repayInterestYes) {
		this.repayInterestYes = repayInterestYes;
	}

	public String getDebtPlanAccountScale() {
		return debtPlanAccountScale;
	}

	public void setDebtPlanAccountScale(String debtPlanAccountScale) {
		this.debtPlanAccountScale = debtPlanAccountScale;
	}
	
}
