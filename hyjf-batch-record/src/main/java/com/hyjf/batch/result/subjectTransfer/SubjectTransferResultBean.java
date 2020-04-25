package com.hyjf.batch.result.subjectTransfer;

public class SubjectTransferResultBean {

	/**
	 * P2P端产品编号
	 */
	private String borrowId;
	/**
	 * 标的编号
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
	private String amount;
	/**
	 * 处理响应码
	 */
	private String respCode;


	
	public String getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(String borrowId) {
		this.borrowId = borrowId;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
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

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

}
