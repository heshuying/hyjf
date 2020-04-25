package com.hyjf.batch.debtTransfer;

import java.math.BigDecimal;

public class DebtTransferBean {

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
	 * 8.债权获取日期
	 */
	private String debtObtDate;
	/**
	 * 9.起息日
	 */
	private String intStDate;
	/**
	 * 10.付息方式
	 */
	private Integer intStStyle;
	/**
	 * 11.利息每月支付日
	 */
	private String intPaydate;
	/**
	 * 12.产品到期日
	 */
	private String endDate;
	/**
	 * 13.预计年化收益率
	 */
	private BigDecimal expectAnualRate;
	/**
	 * 14.币种
	 */
	private String currType;
	/**
	 * 15.保留域
	 */
	private String revers;
	/**
	 * 16.订单号
	 */
	private String orderId;
	/**
	 * 15.类型
	 */
	private String type;
	// add new 4 variable
	/**
	 * 16.借款人用户id
	 */
	private Integer borrowUserId;
	/**
	 * 17.出借人用户id
	 */
	private Integer tenUserId;
	/**
	 * 18.待还利息
	 */
	private BigDecimal interestWait;
	/**
	 * 18.已还利息
	 */
	private BigDecimal interestPaid;
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

	public String getDebtObtDate() {
		return debtObtDate;
	}

	public void setDebtObtDate(String debtObtDate) {
		this.debtObtDate = debtObtDate;
	}

	public String getIntStDate() {
		return intStDate;
	}

	public void setIntStDate(String intStDate) {
		this.intStDate = intStDate;
	}

	public Integer getIntStStyle() {
		return intStStyle;
	}

	public void setIntStStyle(Integer intStStyle) {
		this.intStStyle = intStStyle;
	}

	public String getIntPaydate() {
		return intPaydate;
	}

	public void setIntPaydate(String intPaydate) {
		this.intPaydate = intPaydate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getExpectAnualRate() {
		return expectAnualRate;
	}

	public void setExpectAnualRate(BigDecimal expectAnualRate) {
		this.expectAnualRate = expectAnualRate;
	}

	public String getCurrType() {
		return currType;
	}

	public void setCurrType(String currType) {
		this.currType = currType;
	}

	public String getRevers() {
		return revers;
	}

	public void setRevers(String revers) {
		this.revers = revers;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public Integer getBorrowUserId() {
		return borrowUserId;
	}

	public void setBorrowUserId(Integer borrowUserId) {
		this.borrowUserId = borrowUserId;
	}

	public Integer getTenUserId() {
		return tenUserId;
	}

	public void setTenUserId(Integer tenUserId) {
		this.tenUserId = tenUserId;
	}

	public BigDecimal getInterestWait() {
		return interestWait;
	}

	public void setInterestWait(BigDecimal interestWait) {
		this.interestWait = interestWait;
	}

	public BigDecimal getInterestPaid() {
		return interestPaid;
	}

	public void setInterestPaid(BigDecimal interestPaid) {
		this.interestPaid = interestPaid;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

}
