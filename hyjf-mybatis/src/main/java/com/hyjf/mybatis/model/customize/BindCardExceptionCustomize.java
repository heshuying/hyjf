package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

public class BindCardExceptionCustomize implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4292081468167381347L;
	// 用户ID
	private String userId;
	// 用户名
	private String userName;
	// 电子账户号
	private String accountId;
	// 真实姓名
	private String trueName;
	// 银行卡号
	private String cardNo;
	// 银行名称
	private String bankName;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

}
