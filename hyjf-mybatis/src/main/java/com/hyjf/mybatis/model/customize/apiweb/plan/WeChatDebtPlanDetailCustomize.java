/**
 * Description:计划详情
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize.apiweb.plan;

import java.io.Serializable;

/**
 * @author 王坤
 */

public class WeChatDebtPlanDetailCustomize implements Serializable {

	/** 序列化id */
	private static final long serialVersionUID = -6722330566314459673L;
	// 计划编号
	private String planNid;
	// 计划金额
	private String planAccount;
	// 预期收益率
	private String planApr;
	// 计划期限
	private String planPeriod;
	// 剩余可投金额
	private String planAccountWait;
	// 计划状态:0:待开放,1:募集中,2:锁定中,3:已退出
	private String planStatus;
	// 开始申购时间
	private String buyBeginTime;
	// 申购结束时间
	private String buyEndTime;
	// 满标或者到期时间
	private String fullExpireTime;
	// 退出时间
	private String liquidateFactTime;
	// 退出天数
	private String debtQuitPeriod;
	// 加入进度
	private String planSchedule;

	public WeChatDebtPlanDetailCustomize() {
		super();
	}

	public String getPlanNid() {
		return planNid;
	}

	public void setPlanNid(String planNid) {
		this.planNid = planNid;
	}

	public String getPlanAccount() {
		return planAccount;
	}

	public void setPlanAccount(String planAccount) {
		this.planAccount = planAccount;
	}

	public String getPlanApr() {
		return planApr;
	}

	public void setPlanApr(String planApr) {
		this.planApr = planApr;
	}

	public String getPlanPeriod() {
		return planPeriod;
	}

	public void setPlanPeriod(String planPeriod) {
		this.planPeriod = planPeriod;
	}

	public String getPlanAccountWait() {
		return planAccountWait;
	}

	public void setPlanAccountWait(String planAccountWait) {
		this.planAccountWait = planAccountWait;
	}

	public String getBuyBeginTime() {
		return buyBeginTime;
	}

	public void setBuyBeginTime(String buyBeginTime) {
		this.buyBeginTime = buyBeginTime;
	}

	public String getBuyEndTime() {
		return buyEndTime;
	}

	public void setBuyEndTime(String buyEndTime) {
		this.buyEndTime = buyEndTime;
	}

	public String getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}

	public String getFullExpireTime() {
		return fullExpireTime;
	}

	public void setFullExpireTime(String fullExpireTime) {
		this.fullExpireTime = fullExpireTime;
	}

	public String getLiquidateFactTime() {
		return liquidateFactTime;
	}

	public void setLiquidateFactTime(String liquidateFactTime) {
		this.liquidateFactTime = liquidateFactTime;
	}

	public String getDebtQuitPeriod() {
		return debtQuitPeriod;
	}

	public void setDebtQuitPeriod(String debtQuitPeriod) {
		this.debtQuitPeriod = debtQuitPeriod;
	}

	public String getPlanSchedule() {
		return planSchedule;
	}

	public void setPlanSchedule(String planSchedule) {
		this.planSchedule = planSchedule;
	}

}
