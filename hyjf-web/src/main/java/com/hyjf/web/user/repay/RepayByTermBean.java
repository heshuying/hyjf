package com.hyjf.web.user.repay;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRepay;

public class RepayByTermBean extends BorrowRepay implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 4073070104153180850L;
	// 当前还款期数
	public String borrowPeriod;
	// 用户的ip地址
	private String ip;
	/**
	 * 垫付机构ID
	 */
	public Integer repayUserId;

	/** 用户还款详情 */
	private List<BorrowRecover> recoverList = new ArrayList<BorrowRecover>();

	/** 用户还款详情 */
	private List<RepayplanRecoverplanBean> repayPlanList = new ArrayList<RepayplanRecoverplanBean>();

	public RepayByTermBean() {
		super();
	}

	public List<BorrowRecover> getRecoverList() {
		return recoverList;
	}

	public void setRecoverList(List<BorrowRecover> recoverList) {
		this.recoverList = recoverList;
	}

	public List<RepayplanRecoverplanBean> getRepayPlanList() {
		return repayPlanList;
	}

	public void setRepayPlanList(List<RepayplanRecoverplanBean> repayPlanList) {
		this.repayPlanList = repayPlanList;
	}

	public String getBorrowPeriod() {
		return borrowPeriod;
	}

	public void setBorrowPeriod(String borrowPeriod) {
		this.borrowPeriod = borrowPeriod;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getRepayUserId() {
		return repayUserId;
	}

	public void setRepayUserId(Integer repayUserId) {
		this.repayUserId = repayUserId;
	}

}
