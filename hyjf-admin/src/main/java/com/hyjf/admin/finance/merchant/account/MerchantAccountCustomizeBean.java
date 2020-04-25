package com.hyjf.admin.finance.merchant.account;

import java.io.Serializable;

import com.hyjf.mybatis.model.auto.UserTransfer;

public class MerchantAccountCustomizeBean extends UserTransfer implements Serializable {

	/** 序列化id */
	private static final long serialVersionUID = -432561655172605057L;
	/** 转账账户余额 */
	private String balance;

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

}
