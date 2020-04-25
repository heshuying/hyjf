package com.hyjf.mybatis.model.customize.web;

import java.io.Serializable;

public class InvestRepayCustomize implements Serializable {
	
	/**
	 *序列化id
	 */
	private static final long serialVersionUID = 5748630051215873837L;
	
	/**
	 * 用户ID
	 */
	private String userId;
	
	/**
	 * 账号
	 */
	private String account;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	
}
