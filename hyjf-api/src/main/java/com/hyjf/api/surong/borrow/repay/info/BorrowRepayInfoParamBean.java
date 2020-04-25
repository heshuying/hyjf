package com.hyjf.api.surong.borrow.repay.info;

import com.hyjf.base.bean.BaseBean;


public class BorrowRepayInfoParamBean extends BaseBean {
	
	/**
	 * 标的编号
	 */
	String borrowNid;
	
	/**
	 * 还款期数
	 */
	Integer repayPeriod;
	
	/**
	 * 用户名称
	 */
	String username;
	
	/**
	 * 手机号
	 */
	String mobile;
	
	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public Integer getRepayPeriod() {
		return repayPeriod;
	}

	public void setRepayPeriod(Integer repayPeriod) {
		this.repayPeriod = repayPeriod;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
