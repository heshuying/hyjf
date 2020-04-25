package com.hyjf.mybatis.model.customize;

public class WkcdOverDateCustomize {
	private String borrowId;
	private String borrowIdThirdparty;
	private String overdueAmount;
	private String period;

	public String getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(String borrowId) {
		this.borrowId = borrowId;
	}

	public String getBorrowIdThirdparty() {
		return borrowIdThirdparty;
	}

	public void setBorrowIdThirdparty(String borrowIdThirdparty) {
		this.borrowIdThirdparty = borrowIdThirdparty;
	}

	public String getOverdueAmount() {
		return overdueAmount;
	}

	public void setOverdueAmount(String overdueAmount) {
		this.overdueAmount = overdueAmount;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

}
