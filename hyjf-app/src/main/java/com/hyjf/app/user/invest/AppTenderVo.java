/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.app.user.invest;

import com.hyjf.app.BaseBean;

/**
 * app客户端请求参数对象
 * 
 * @author 王坤
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年2月19日
 * @see 下午1:55:44
 */
public class AppTenderVo extends BaseBean {

	private String borrowNid;

	private String account;

	private String interest;
	
	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

}
