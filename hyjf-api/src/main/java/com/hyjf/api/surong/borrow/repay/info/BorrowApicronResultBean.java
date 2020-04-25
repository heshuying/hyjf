package com.hyjf.api.surong.borrow.repay.info;

import java.math.BigDecimal;

import com.hyjf.base.bean.BaseResultBean;


public class BorrowApicronResultBean extends BaseResultBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3988869679360594579L;
	
	/**
	 * 还款总额
	 */
	BigDecimal repayAccountAll;
	/**
	 * 实际最后还款时间
	 */
	String borrowRepayYestime;
	
	/**
	 * 还款状态
	 */
	Integer repayStatus;

	public BigDecimal getRepayAccountAll() {
		return repayAccountAll;
	}

	public void setRepayAccountAll(BigDecimal repayAccountAll) {
		this.repayAccountAll = repayAccountAll;
	}

	public Integer getRepayStatus() {
		return repayStatus;
	}

	public void setRepayStatus(Integer repayStatus) {
		this.repayStatus = repayStatus;
	}

	public String getBorrowRepayYestime() {
		return borrowRepayYestime;
	}

	public void setBorrowRepayYestime(String borrowRepayYestime) {
		this.borrowRepayYestime = borrowRepayYestime;
	}

	
}
