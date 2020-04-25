package com.hyjf.mybatis.model.customize.batch;

import java.io.Serializable;

public class BatchDebtPlanAccedeCustomize implements Serializable {

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = -2386279131259439436L;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 协议编号
	 */
	private String orderId;

	/**
	 * 计划编号
	 */
	private String planNid;
	/**
	 * 签订日期
	 */
	private String signedDate;

	/**
	 * 姓名
	 */
	private String realName;

	/**
	 * 身份证号
	 */
	private String idCard;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 加入金额
	 */
	private String accedeAccount;

	/**
	 * 计划期限
	 */
	private String debtLockPeriod;

	/**
	 * 预期年化收益
	 */
	private String expectApr;

	/**
	 * 生效日期
	 */
	private String effectiveDate;

	/**
	 * 到期日期
	 */
	private String expireDate;

	/**
	 * 收益处理方式
	 */
	private String profitHandleStyle;
	/**
	 * 本金
	 */
	private String capital;

	/**
	 * 利息
	 */
	private String interest;

	/**
	 * 最低加入金额
	 */
	private String debtMinInvestment;

	/**
	 * 递增金额
	 */
	private String investmentIncrement;

	/**
	 * 最高加入金额
	 */
	private String maxInvestment;

	/**
	 * 退出天数
	 */
	private String quitPeriod;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSignedDate() {
		return signedDate;
	}

	public void setSignedDate(String signedDate) {
		this.signedDate = signedDate;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getAccedeAccount() {
		return accedeAccount;
	}

	public void setAccedeAccount(String accedeAccount) {
		this.accedeAccount = accedeAccount;
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

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getProfitHandleStyle() {
		return profitHandleStyle;
	}

	public void setProfitHandleStyle(String profitHandleStyle) {
		this.profitHandleStyle = profitHandleStyle;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getDebtMinInvestment() {
		return debtMinInvestment;
	}

	public void setDebtMinInvestment(String debtMinInvestment) {
		this.debtMinInvestment = debtMinInvestment;
	}

	public String getInvestmentIncrement() {
		return investmentIncrement;
	}

	public void setInvestmentIncrement(String investmentIncrement) {
		this.investmentIncrement = investmentIncrement;
	}

	public String getMaxInvestment() {
		return maxInvestment;
	}

	public void setMaxInvestment(String maxInvestment) {
		this.maxInvestment = maxInvestment;
	}

	public String getQuitPeriod() {
		return quitPeriod;
	}

	public void setQuitPeriod(String quitPeriod) {
		this.quitPeriod = quitPeriod;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPlanNid() {
		return planNid;
	}

	public void setPlanNid(String planNid) {
		this.planNid = planNid;
	}

}
