package com.hyjf.batch.sigtranTransfer;

public class SigtranTransferBean {
	/**
	 * 1.银行代号
	 */
	private String bankId;
	/**
	 * 2.批次号
	 */
	private String batchId;
	/**
	 * 3.签约电子账号
	 */
	private String sigCardnnbr;
	/**
	 * 4.产品发行方
	 */
	private String prodFuissuer;
	/**
	 * 5.签约类型
	 */
	private String sigType;
	/**
	 * 6.签约流水号
	 */
	private String sigNo;
	/**
	 * 7.签约日期
	 */
	private String sigDate;
	/**
	 * 8.签约时间
	 */
	private String sigTime;
	/**
	 * 9.保留域
	 */
	private String sigReserved;
	/**
	 * 10.第三方保留域
	 */
	private String sigTrdresv;	
	/**
	 * 11.订单号
	 * */
	private String orderId;
	/**
	 * 12.申请签约流水号
	 */
	private String serialNum;
	/**
	 * 13.用户user_id
	 * */
	private String userId;
	
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
	public String getSigCardnnbr() {
		return sigCardnnbr;
	}
	public void setSigCardnnbr(String sigCardnnbr) {
		this.sigCardnnbr = sigCardnnbr;
	}
	public String getProdFuissuer() {
		return prodFuissuer;
	}
	public void setProdFuissuer(String prodFuissuer) {
		this.prodFuissuer = prodFuissuer;
	}
	public String getSigType() {
		return sigType;
	}
	public String setSigType(String sigType) {
		return this.sigType = sigType;
	}
	public String getSigNo() {
		return sigNo;
	}
	public void setSigNo(String sigNo) {
		this.sigNo = sigNo;
	}
	public String getSigDate() {
		return sigDate;
	}
	public void setSigDate(String sigDate) {
		this.sigDate = sigDate;
	}
	public String getSigTime() {
		return sigTime;
	}
	public void setSigTime(String sigTime) {
		this.sigTime = sigTime;
	}
	public String getSigReserved() {
		return sigReserved;
	}
	public void setSigReserved(String sigReserved) {
		this.sigReserved = sigReserved;
	}
	public String getSigTrdresv() {
		return sigTrdresv;
	}
	public void setSigTrdresv(String sigTrdresv) {
		this.sigTrdresv = sigTrdresv;
	}

	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
