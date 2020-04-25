package com.hyjf.api.surong.borrow.status;

import java.math.BigDecimal;

import com.hyjf.base.bean.BaseResultBean;


public class BorrowRepayPlanResultBean extends BaseResultBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3988869679360594579L;

	/**
	 * 标的编号
	 */
	String borrowNid;
	
	/**
	 * 还款期次
	 */
	Integer repayPeriod;
	
	/**
	 * 还款金额（包含管理费）
	 */
	BigDecimal repayAccountAll;
	
	/**
	 * 应还款时间
	 */
	String repayTime;
	
	/**
	 * 还款状态
	 */
	Integer repayStatus;

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

	public BigDecimal getRepayAccountAll() {
		return repayAccountAll;
	}

	public void setRepayAccountAll(BigDecimal repayAccountAll) {
		this.repayAccountAll = repayAccountAll;
	}

	public String getRepayTime() {
		return repayTime;
	}

	public void setRepayTime(String repayTime) {
		this.repayTime = repayTime;
	}

	public Integer getRepayStatus() {
		return repayStatus;
	}

	public void setRepayStatus(Integer repayStatus) {
		this.repayStatus = repayStatus;
	}
	
	

}
