package com.hyjf.batch.result.sigtranTransfer;

public class SigtranTransferResultBean {
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
	 * 9.响应码
	 */
	private String rspCode;
	/**
	 * 10.保留域
	 */
	private String reserved;
	/**
	 * 11.第三方保留域
	 */
	private String trdResv;
	
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
	
	public void setSigType(String sigType) {
		this.sigType = sigType;
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
	
	public String getRspCode() {
		return rspCode;
	}
	
	public void setRspCode(String rspCode) {
		this.rspCode = rspCode;
	}
	
	public String getReserved() {
		return reserved;
	}
	
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	
	public String getTrdResv() {
		return trdResv;
	}
	
	public void setTrdResv(String trdResv) {
		this.trdResv = trdResv;
	}
	
	public String getTrdresv() {
		return trdResv;
	}
	
	public void setTrdresv(String trdresv) {
		this.trdResv = trdresv;
	}

}
