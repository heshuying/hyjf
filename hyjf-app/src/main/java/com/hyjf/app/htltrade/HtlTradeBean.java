package com.hyjf.app.htltrade;

import java.io.Serializable;

public class HtlTradeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//类型：1转入，2转出
	public String tradeType;
	
	public String createTime;
	
	public String amount;

	
	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
