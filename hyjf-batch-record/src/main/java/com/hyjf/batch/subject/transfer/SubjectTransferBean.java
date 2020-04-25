package com.hyjf.batch.subject.transfer;

import java.math.BigDecimal;

public class SubjectTransferBean {

	/**
	 * P2P端产品编号
	 */
	private String p2pProdId;
	
	/**
	 * 借款编号
	 */
	private String borrowNid;
	
	/**
	 * 产品描述
	 */
	private String prodDesc;
	/**
	 * 借款人电子账号
	 */
	private String borrowerElecAcc;
	/**
	 * 借款金额
	 */
	private BigDecimal amount;
	/**
	 * 付息方式
	 */
	private Integer paymentStyle;
	/**
	 * 利息每月支付日
	 */
	private String interestPayDate;
	/**
	 * 项目期限
	 */
	private Integer period;
	/**
	 * 预计年化收益率
	 */
	private BigDecimal expectAnnualRate;
	/**
	 * 担保人电子账号
	 */
	private String guarantorElecAcc;
	/**
	 * 名义借款人电子账号
	 */
	private String nominalElecAcc;
	/**
	 * 募集日
	 */
	private String raiseDate;
	/**
	 * 募集结束日期
	 */
	private String raiseEndDate;

	public String getP2pProdId() {
		return p2pProdId;
	}

	public void setP2pProdId(String p2pProdId) {
		this.p2pProdId = p2pProdId;
	}

	public String getProdDesc() {
		return prodDesc;
	}

	public void setProdDesc(String prodDesc) {
		this.prodDesc = prodDesc;
	}

	public String getBorrowerElecAcc() {
		return borrowerElecAcc;
	}

	public void setBorrowerElecAcc(String borrowerElecAcc) {
		this.borrowerElecAcc = borrowerElecAcc;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getInterestPayDate() {
		return interestPayDate;
	}

	public void setInterestPayDate(String interestPayDate) {
		this.interestPayDate = interestPayDate;
	}

	public BigDecimal getExpectAnnualRate() {
		return expectAnnualRate;
	}

	public void setExpectAnnualRate(BigDecimal expectAnnualRate) {
		this.expectAnnualRate = expectAnnualRate;
	}

	public String getGuarantorElecAcc() {
		return guarantorElecAcc;
	}

	public void setGuarantorElecAcc(String guarantorElecAcc) {
		this.guarantorElecAcc = guarantorElecAcc;
	}

	public String getNominalElecAcc() {
		return nominalElecAcc;
	}

	public void setNominalElecAcc(String nominalElecAcc) {
		this.nominalElecAcc = nominalElecAcc;
	}

	public String getRaiseDate() {
		return raiseDate;
	}

	public void setRaiseDate(String raiseDate) {
		this.raiseDate = raiseDate;
	}

	public String getRaiseEndDate() {
		return raiseEndDate;
	}

	public void setRaiseEndDate(String raiseEndDate) {
		this.raiseEndDate = raiseEndDate;
	}

	public Integer getPaymentStyle() {
		return paymentStyle;
	}

	public void setPaymentStyle(Integer paymentStyle) {
		this.paymentStyle = paymentStyle;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

}
