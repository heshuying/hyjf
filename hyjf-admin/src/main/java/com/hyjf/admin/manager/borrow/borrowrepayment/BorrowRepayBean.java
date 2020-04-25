package com.hyjf.admin.manager.borrow.borrowrepayment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRepay;

public class BorrowRepayBean extends BorrowRepay implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 4073070104153180850L;
	/** 用户还款详情 */
	private List<BorrowRecover> recoverList = new ArrayList<BorrowRecover>();

	private String repayTimeStr;

	private String borrowStatus;

	private String ip;

	public BorrowRepayBean() {
		super();
	}

	public List<BorrowRecover> getRecoverList() {
		return recoverList;
	}

	public void setRecoverList(List<BorrowRecover> recoverList) {
		this.recoverList = recoverList;
	}

	public String getRepayTimeStr() {
		return repayTimeStr;
	}

	public void setRepayTimeStr(String repayTimeStr) {
		this.repayTimeStr = repayTimeStr;
	}

	public String getBorrowStatus() {
		return borrowStatus;
	}

	public void setBorrowStatus(String borrowStatus) {
		this.borrowStatus = borrowStatus;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
