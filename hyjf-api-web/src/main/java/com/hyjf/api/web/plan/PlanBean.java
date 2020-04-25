package com.hyjf.api.web.plan;

import com.hyjf.base.bean.BaseBean;

public class PlanBean extends BaseBean {
	// 计划编号
	private String planNid;

	// 出借金额
	private String account;

	// 授权参数
	private String appointment;
	// 回调
	private String callback;

	public String getPlanNid() {
		return planNid;
	}

	public void setPlanNid(String planNid) {
		this.planNid = planNid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAppointment() {
		return appointment;
	}

	public void setAppointment(String appointment) {
		this.appointment = appointment;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

}
