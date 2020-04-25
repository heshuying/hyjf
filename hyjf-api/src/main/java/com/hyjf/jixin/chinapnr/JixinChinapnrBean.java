package com.hyjf.jixin.chinapnr;

import java.io.Serializable;

import com.hyjf.base.bean.BaseBean;

public class JixinChinapnrBean implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2569482809422132226L;
	
	/**
	 * 交易日期
	 */
	private Integer txDate;
	/**
	 * 交易时间
	 */
	private Integer txTime;
	/**
	 * 交易流水号
	 */
	private String seqNo;
	/**
	 * 请求时间戳
	 */
	private String timestamp;
	/**
	 * md5校验码
	 */
	private String sign;
	public Integer getTxDate() {
		return txDate;
	}
	public void setTxDate(Integer txDate) {
		this.txDate = txDate;
	}
	public Integer getTxTime() {
		return txTime;
	}
	public void setTxTime(Integer txTime) {
		this.txTime = txTime;
	}
	public String getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
	
	
}
