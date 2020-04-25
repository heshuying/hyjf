package com.hyjf.api.server.user.openaccount;

import com.hyjf.base.bean.BaseResultBean;

/**
 * 用户开户结果Bean
 * 
 * @author liuyang
 *
 */
public class UserOpenAccountResultBean extends BaseResultBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -821943657829999082L;
	// 发送短信的orderId
	private String orderId;
	// 用户是否开户
	private String isOpenAccount;
	// 电子账户号
	private String accountId;
    //银行卡联行号
	private String payAllianceCode;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getIsOpenAccount() {
		return isOpenAccount;
	}

	public void setIsOpenAccount(String isOpenAccount) {
		this.isOpenAccount = isOpenAccount;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getPayAllianceCode() {
		return payAllianceCode;
	}

	public void setPayAllianceCode(String payAllianceCode) {
		this.payAllianceCode = payAllianceCode;
	}


}
