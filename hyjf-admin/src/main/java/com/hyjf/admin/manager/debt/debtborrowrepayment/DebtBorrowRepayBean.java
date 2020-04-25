package com.hyjf.admin.manager.debt.debtborrowrepayment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hyjf.mybatis.model.auto.DebtLoan;
import com.hyjf.mybatis.model.auto.DebtRepay;

public class DebtBorrowRepayBean extends DebtRepay implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 4073070104153180850L;
	/** 用户还款详情 */
	private List<DebtLoan> recoverList = new ArrayList<DebtLoan>();

	private String repayTimeStr;

	private String borrowStatus;
	
	private String ip;
	
	public DebtBorrowRepayBean() {
		super();
	}

	public List<DebtLoan> getRecoverList() {
		return recoverList;
	}

	public void setRecoverList(List<DebtLoan> recoverList) {
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
