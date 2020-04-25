package com.hyjf.admin.manager.borrow.borrowrepayment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;

public class BorrowRepayPlanBean extends BorrowRepayPlan implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 4073070104153180850L;
	/** 用户还款详情 */
	private List<BorrowRecoverPlan> recoverPlanList = new ArrayList<BorrowRecoverPlan>();
	
	private String repayTimeStr;
	
	private String borrowStatus;
	
	private String ip;

	public BorrowRepayPlanBean() {
		super();
	}

	public List<BorrowRecoverPlan> getRecoverPlanList() {
		return recoverPlanList;
	}

	public void setRecoverPlanList(List<BorrowRecoverPlan> recoverPlanList) {
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
