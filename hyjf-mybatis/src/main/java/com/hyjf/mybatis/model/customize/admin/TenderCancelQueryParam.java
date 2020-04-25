package com.hyjf.mybatis.model.customize.admin;

import com.hyjf.mybatis.model.auto.BorrowTenderTmp;

public class TenderCancelQueryParam extends BorrowTenderTmp{

	
	private static final long serialVersionUID = 6295994427288581318L;

	private int limitStart;
	
	private int limitEnd;
	
	private String orderByClause;

	
	public String getOrderByClause() {
		return orderByClause;
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	public int getLimitStart() {
		return limitStart;
	}

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	public int getLimitEnd() {
		return limitEnd;
	}

	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

	
}
