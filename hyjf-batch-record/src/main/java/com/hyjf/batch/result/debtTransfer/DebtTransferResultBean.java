package com.hyjf.batch.result.debtTransfer;

import java.math.BigDecimal;

public class DebtTransferResultBean {

	/**
	 * 1.银行代号
	 */
	private String bankId;
	/**
	 * 2.批次号
	 */
	private String batchId;
	/**
	 * 3.债权持有人电子账号
	 */
	private String debtHolderAcc;
	/**
	 * 4.产品发行方
	 */
	private String prodIssuer;
	/**
	 * 5.产品编号
	 */
	private String prodNum;
	/**
	 * 6.申请流水号
	 */
	private String serialNum;
	/**
	 * 7.当前持有债权金额
	 */
	private BigDecimal amount;
	/**
	 * 8.持卡人姓名
	 */
	private String holderName;
	/**
	 * 9.处理日期
	 */
	private String dealDate;
	/**
	 * 10.处理响应码
	 */
	private String respCode;
	/**
	 * 11.申请授权码
	 */
	private String authCode;
	/**
	 * 12.保留域
	 */
	private String revers;
	/**
	 * 标的号
	 */
	private String borrowNid;
	

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getDebtHolderAcc() {
		return debtHolderAcc;
	}

	public void setDebtHolderAcc(String debtHolderAcc) {
		this.debtHolderAcc = debtHolderAcc;
	}

	public String getProdIssuer() {
		return prodIssuer;
	}

	public void setProdIssuer(String prodIssuer) {
		this.prodIssuer = prodIssuer;
	}

	public String getProdNum() {
		return prodNum;
	}

	public void setProdNum(String prodNum) {
		this.prodNum = prodNum;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getHolderName() {
		return holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	public String getDealDate() {
		return dealDate;
	}

	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getRevers() {
		return revers;
	}

	public void setRevers(String revers) {
		this.revers = revers;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

}
