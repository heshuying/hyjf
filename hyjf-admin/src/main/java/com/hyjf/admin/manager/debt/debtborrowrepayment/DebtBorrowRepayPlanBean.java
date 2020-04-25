package com.hyjf.admin.manager.debt.debtborrowrepayment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hyjf.mybatis.model.auto.DebtLoanDetail;
import com.hyjf.mybatis.model.auto.DebtRepayDetail;

public class DebtBorrowRepayPlanBean extends DebtRepayDetail implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 4073070104153180850L;
	/** 用户还款详情 */
	private List<DebtLoanDetail> recoverPlanList = new ArrayList<DebtLoanDetail>();

	private String repayTimeStr;

	private String borrowStatus;

	private String ip;

	public DebtBorrowRepayPlanBean() {
		super();
	}

	public List<DebtLoanDetail> getRecoverPlanList() {
		return recoverPlanList;
	}

	public void setRecoverPlanList(List<DebtLoanDetail> recoverPlanList) {
		this.recoverPlanList = recoverPlanList;
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
