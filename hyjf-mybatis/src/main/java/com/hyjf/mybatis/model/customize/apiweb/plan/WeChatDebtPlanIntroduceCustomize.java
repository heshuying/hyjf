/**
 * Description:计划介绍
 * Copyright: Copyright (HYJF Corporation)2016
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2016年10月9日 下午1:33:42
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize.apiweb.plan;

import java.io.Serializable;

/**
 * @author Administrator
 */

public class WeChatDebtPlanIntroduceCustomize implements Serializable {

	/** 序列化id */
	private static final long serialVersionUID = 6334088126819304494L;
	// 计划名称
	private String planName;
	// 计划概念
	private String planConcept;
	// 计划原理
	private String planTheory;
	// 计划最小加入金额
	private String planAccedeMinAccount;
	// 计划倍增金额
	private String planAccedeIncreaseAccount;
	// 计划最大加入金额
	private String planAccedeMaxAccount;
	// 计划锁定时间
	private String planLockPeriod;
	// 计划还款时间间隔
	private String planRepayPeriod;//?什么意思

	/** 构造方法 */
	public WeChatDebtPlanIntroduceCustomize() {
		super();
	}

	public String getPlanConcept() {
		return planConcept;
	}

	public void setPlanConcept(String planConcept) {
		this.planConcept = planConcept;
	}

	public String getPlanTheory() {
		return planTheory;
	}

	public void setPlanTheory(String planTheory) {
		this.planTheory = planTheory;
	}

	public String getPlanAccedeMinAccount() {
		return planAccedeMinAccount;
	}

	public void setPlanAccedeMinAccount(String planAccedeMinAccount) {
		this.planAccedeMinAccount = planAccedeMinAccount;
	}

	public String getPlanAccedeIncreaseAccount() {
		return planAccedeIncreaseAccount;
	}

	public void setPlanAccedeIncreaseAccount(String planAccedeIncreaseAccount) {
		this.planAccedeIncreaseAccount = planAccedeIncreaseAccount;
	}

	public String getPlanAccedeMaxAccount() {
		return planAccedeMaxAccount;
	}

	public void setPlanAccedeMaxAccount(String planAccedeMaxAccount) {
		this.planAccedeMaxAccount = planAccedeMaxAccount;
	}

	public String getPlanLockPeriod() {
		return planLockPeriod;
	}

	public void setPlanLockPeriod(String planLockPeriod) {
		this.planLockPeriod = planLockPeriod;
	}

	public String getPlanRepayPeriod() {
		return planRepayPeriod;
	}

	public void setPlanRepayPeriod(String planRepayPeriod) {
		this.planRepayPeriod = planRepayPeriod;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

}
