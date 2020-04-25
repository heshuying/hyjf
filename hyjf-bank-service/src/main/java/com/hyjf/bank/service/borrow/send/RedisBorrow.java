package com.hyjf.bank.service.borrow.send;

import java.math.BigDecimal;

public class RedisBorrow {

    private String borrowNid;
    private BigDecimal borrowAccountWait;
    
	public String getBorrowNid() {
		return borrowNid;
	}
	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}
	public BigDecimal getBorrowAccountWait() {
		return borrowAccountWait;
	}
	public void setBorrowAccountWait(BigDecimal borrowAccountWait) {
		this.borrowAccountWait = borrowAccountWait;
	}
	
}
