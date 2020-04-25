/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月20日 下午5:24:10
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

/**
 * @author Administrator
 */

public class PlanCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;
	/**
	 * 计划id
	 */
	private String id;
	/**
	 * 计划编号
	 */
	private String planNid;
	/**
	 * 计划名称
	 */
	private String planName;
	/**
	 * 计划类型id
	 */
	private String planType;
	/**
	 * 计划类型名称
	 */
	private String planTypeName;
	/**
	 * 计划锁定期
	 */
	private String lockPeriod;
	/**
	 * 预期年化
	 */
	private String expectApr;
	/**
	 * 计划总额
	 */
	private String planMoney;
	/**
	 * 计划加入金额
	 */
	private String planJoinMoney;
	/**
	 * 计划剩余金额
	 */
	private String planMoneyWait;
	/**
	 * 计划状态  0 发起中；1 待审核；2审核不通过；3待开放；4募集中；5募集完成；6锁定中；7清算中；8清算完成，9还款，10还款中，11还款完成
	 */
	private String planStatus;
	/**
	 * 计划状态名称
	 */
	private String planStatusName;
	/**
	 * 计划发起时间
	 */
	private String createTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlanNid() {
		return planNid;
	}
	public void setPlanNid(String planNid) {
		this.planNid = planNid;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getPlanType() {
		return planType;
	}
	public void setPlanType(String planType) {
		this.planType = planType;
	}
	public String getPlanTypeName() {
		return planTypeName;
	}
	public void setPlanTypeName(String planTypeName) {
		this.planTypeName = planTypeName;
	}
	public String getLockPeriod() {
		return lockPeriod;
	}
	public void setLockPeriod(String lockPeriod) {
		this.lockPeriod = lockPeriod;
	}
	public String getExpectApr() {
		return expectApr;
	}
	public void setExpectApr(String expectApr) {
		this.expectApr = expectApr;
	}
	public String getPlanMoney() {
		return planMoney;
	}
	public void setPlanMoney(String planMoney) {
		this.planMoney = planMoney;
	}
	public String getPlanJoinMoney() {
		return planJoinMoney;
	}
	public void setPlanJoinMoney(String planJoinMoney) {
		this.planJoinMoney = planJoinMoney;
	}
	public String getPlanMoneyWait() {
		return planMoneyWait;
	}
	public void setPlanMoneyWait(String planMoneyWait) {
		this.planMoneyWait = planMoneyWait;
	}
	public String getPlanStatus() {
		return planStatus;
	}
	public void setPlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getPlanStatusName() {
		return planStatusName;
	}
	public void setPlanStatusName(String planStatusName) {
		this.planStatusName = planStatusName;
	}
	
}
