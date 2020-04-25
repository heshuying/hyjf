/**
 * Description:计划列表
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

public class WeChatDebtPlanCustomize implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 5748630051215873837L;
	// 计划nid
	private String planNid;
	// 计划年华收益率
	private String planApr;
	// 计划
	private String planPeriod;
	// 计划金额（万）
	private String planAccount;
	// 计划进度
	private String planSchedule;
	// 计划状态:0:待开放,1:募集中,2:锁定中,3:已退出
	private String status;
	// 计划开始时间
	private String buyBeginTime;
	// 计划名称
	private String planName;

	public WeChatDebtPlanCustomize() {
		super();
	}

	public String getPlanNid() {
		return planNid;
	}

	public void setPlanNid(String planNid) {
		this.planNid = planNid;
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

	public String getPlanAccount() {
		return planAccount;
	}

	public void setPlanAccount(String planAccount) {
		this.planAccount = planAccount;
	}

	public String getPlanSchedule() {
		return planSchedule;
	}

	public void setPlanSchedule(String planSchedule) {
		this.planSchedule = planSchedule;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBuyBeginTime() {
		return buyBeginTime;
	}

	public void setBuyBeginTime(String buyBeginTime) {
		this.buyBeginTime = buyBeginTime;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

}
