package com.hyjf.api.surong.borrow.status;

import com.hyjf.base.bean.BaseBean;


public class BorrowInfoSynParamBean extends BaseBean {
	
	/**
	 * 标的编号
	 */
	String borrowNid;
	
	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

}
