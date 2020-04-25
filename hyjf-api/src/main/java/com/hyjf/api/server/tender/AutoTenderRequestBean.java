package com.hyjf.api.server.tender;

import com.hyjf.base.bean.BaseBean;

/**
 * 自动出借请求参数
 */
public class AutoTenderRequestBean extends BaseBean {
	
	private String couponGrantId;
	
	private String borrowNid;
	
	private String money;
	
	private String accountId;

	public String getCouponGrantId() {
		return couponGrantId;
	}

	public void setCouponGrantId(String couponGrantId) {
		this.couponGrantId = couponGrantId;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
}
