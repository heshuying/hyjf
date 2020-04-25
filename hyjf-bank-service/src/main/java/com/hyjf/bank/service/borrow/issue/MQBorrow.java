package com.hyjf.bank.service.borrow.issue;


public class MQBorrow {

    private String borrowNid;
    private String creditNid;
    
	public String getBorrowNid() {
		return borrowNid;
	}
	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}
	
	public String getCreditNid() {
		return creditNid;
	}
	public void setCreditNid(String creditNid) {
		this.creditNid = creditNid;
	}
}
