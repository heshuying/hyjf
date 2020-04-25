package com.hyjf.api.aems.repay;

import com.hyjf.base.bean.BaseBean;

public class AemsRepayParamBean extends BaseBean{
	//查询类型 0:待还款 1:已还款
	private String repayType;
	//标的号
	private String borrowNid;
	//资产编号
	private String productId;

	// 应还时间开始
	public String startDate;
	// 应还时间结束
	public String endDate;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getRepayType() {
		return repayType;
	}

	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
