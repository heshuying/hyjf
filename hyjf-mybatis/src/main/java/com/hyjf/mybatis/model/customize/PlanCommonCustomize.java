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
import java.math.BigDecimal;

/**
 * @author Administrator
 */

public class PlanCommonCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;
	/**
	 * 排序
	 */
	private String sort;
	/**
	 * 排序列
	 */
	private String col;

	/**
	 * 检索条件 计划编号
	 */
	private String planNidSrch;

	/**
	 * 检索条件 计划名称
	 */
	private String planNameSrch;
	/**
	 * 检索条件 计划类型 “配置中心-汇添金配置”中所有计划类型（不区分启用禁用）
	 */
	private String planTypeSrch;

	/**
	 * 检索条件 计划状态 全部；0 发起中；1
	 * 待审核；2审核不通过；3待开放；4募集中；5募集完成；6锁定中；7清算中；8清算完成，9还款，10还款中，11还款完成
	 */
	private String planStatusSrch;

	/**
	 * 检索条件 时间开始
	 */
	private String timeStartSrch;

	/**
	 * 检索条件
	 */
	private String timeEndSrch;
	/**
	 * 检索条件 满标/到期时间
	 */
	private String fullExpireTime;
	/**
	 * 检索条件 满标/到期时间结束
	 */
	private String fullExpireTimeEnd;
	/**
	 * 检索条件 实际清算时间
	 */
	private String liquidateFactTime;
	/**
	 * 检索条件 实际清算时间 开始
	 */
	private String liquidateFactTimeStart;
	/**
	 * 检索条件 实际清算时间结束
	 */
	private String liquidateFactTimeEnd;
	/**
	 * 检索条件 应清算时间
	 */
	private String liquidateShouldTime;
	/**
	 * 检索条件 应清算时间结束
	 */
	private String liquidateShouldTimeEnd;
	/**
	 * 检索条件 用户名
	 */
	private String userName;
	/**
	 * 检索条件 计划余额最小
	 */
	private String planWaitMoneyMin;
	/**
	 * 检索条件 计划余额最大
	 */
	private String planWaitMoneyMax;
	/**
	 * 检索条件 加入时间开始
	 */
	private String joinTimeStart;
	/**
	 * 检索条件 加入时间结束
	 */
	private String joinTimeEnd;
	/**
	 * 检索条件 订单号
	 */
	private String accedeOrderId;
	/**
	 * 检索条件 项目编号
	 */
	private String borrowNid;
	/**
	 * 检索条件 项目类型
	 */
	private String projectType;
	/**
	 * 检索条件 还款方式
	 */
	private String repayType;
	/**
	 * 检索条件 计划订单号
	 */
	private String planOrderId;
	/**
	 * 检索条件 出借/承接订单号
	 */
	private String orderId;
	/**
	 * 检索条件 回款状态
	 */
	private String repayStatus;
	/**
	 * 检索条件 应回款日期开始
	 */
	private String repayTimeStart;
	/**
	 * 检索条件 应回款日期结束
	 */
	private String repayTimeEnd;
	/**
	 * 检索条件 最迟应还款日期开始
	 */
	private String repayTimeLastStart;
	/**
	 * 检索条件 最迟应还款日期结束
	 */
	private String repayTimeLastEnd;
	/**
	 * 检索条件plan表状态 0 发起中；1 待审核；2审核不通过；3待开放；4募集中；5锁定中；6清算中；7清算完成，8未还款，9还款中，10还款完成
	 */
	private String status;
	/**
	 * 检索条件 用户id
	 */
	private String userId;
	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;
	/**
	 * 检索条件 订单状态
	 */
	private String orderStatusSrch;
	/**
	 * 检索条件 计划实际还款时间开始
	 */
	private String actulRepayTimeStart;
	/**
	 * 检索条件 计划实际还款时间结束
	 */
	private String actulRepayTimeEnd;
	/**
	 * 检索条件 锁定期
	 */
	private String debtLockPeriodSrch;
	/**
	 * 检索条件 还款方式
	 */
	private String borrowStyleSrch;
	/**
	 * 检索条件 推荐人
	 */
	private String refereeNameSrch;
	// EXCEL 导出用
	private String planNid;
	private String planName;
	private String planTypeName;
	private String planMoney;
	private String expectApr;
	private String lockPeriod;
	private String planJoinMoney;
	private String planMoneyWait;
	private String planBalance;
	private String planFrost;
	private String planStatusName;
	private String createTime;
	// 到期公允价值
	private String expireFairValue;
	
	// EXCEL 导出用

	/*--------add by LSY START--------------*/
	/**
	 * 加入金额
	 */
	private BigDecimal sumAccedeAccount;
	
	/**
	 * 应还利息
	 */
	private BigDecimal sumRepayInterest;
	
	/**
	 * 已还款
	 */
	private String sumRepayAlready;
	
	/**
	 * 待还款
	 */
	private BigDecimal sumRepayWait;
	
	/**
	 * 已还本金
	 */
	private String sumPlanRepayCapital;
	
	/**
	 * 已还利息
	 */
	private String sumPlanRepayInterest;
	
	/*--------add by LSY END--------------*/

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getCol() {
		return col;
	}

	public void setCol(String col) {
		this.col = col;
	}

	public String getPlanNidSrch() {
		return planNidSrch;
	}

	public void setPlanNidSrch(String planNidSrch) {
		this.planNidSrch = planNidSrch;
	}

	public String getPlanNameSrch() {
		return planNameSrch;
	}

	public void setPlanNameSrch(String planNameSrch) {
		this.planNameSrch = planNameSrch;
	}

	public String getPlanTypeSrch() {
		return planTypeSrch;
	}

	public void setPlanTypeSrch(String planTypeSrch) {
		this.planTypeSrch = planTypeSrch;
	}

	public String getPlanStatusSrch() {
		return planStatusSrch;
	}

	public void setPlanStatusSrch(String planStatusSrch) {
		this.planStatusSrch = planStatusSrch;
	}

	public String getTimeStartSrch() {
		return timeStartSrch;
	}

	public void setTimeStartSrch(String timeStartSrch) {
		this.timeStartSrch = timeStartSrch;
	}

	public int getLimitStart() {
		return limitStart;
	}

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	public int getLimitEnd() {
		return limitEnd;
	}

	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
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

	public String getPlanTypeName() {
		return planTypeName;
	}

	public void setPlanTypeName(String planTypeName) {
		this.planTypeName = planTypeName;
	}

	public String getPlanMoney() {
		return planMoney;
	}

	public void setPlanMoney(String planMoney) {
		this.planMoney = planMoney;
	}

	public String getExpectApr() {
		return expectApr;
	}

	public void setExpectApr(String expectApr) {
		this.expectApr = expectApr;
	}

	public String getLockPeriod() {
		return lockPeriod;
	}

	public void setLockPeriod(String lockPeriod) {
		this.lockPeriod = lockPeriod;
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

	public String getPlanBalance() {
		return planBalance;
	}

	public void setPlanBalance(String planBalance) {
		this.planBalance = planBalance;
	}

	public String getPlanFrost() {
		return planFrost;
	}

	public void setPlanFrost(String planFrost) {
		this.planFrost = planFrost;
	}

	public String getPlanStatusName() {
		return planStatusName;
	}

	public void setPlanStatusName(String planStatusName) {
		this.planStatusName = planStatusName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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

	public String getLiquidateShouldTime() {
		return liquidateShouldTime;
	}

	public void setLiquidateShouldTime(String liquidateShouldTime) {
		this.liquidateShouldTime = liquidateShouldTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPlanWaitMoneyMin() {
		return planWaitMoneyMin;
	}

	public void setPlanWaitMoneyMin(String planWaitMoneyMin) {
		this.planWaitMoneyMin = planWaitMoneyMin;
	}

	public String getPlanWaitMoneyMax() {
		return planWaitMoneyMax;
	}

	public void setPlanWaitMoneyMax(String planWaitMoneyMax) {
		this.planWaitMoneyMax = planWaitMoneyMax;
	}

	public String getJoinTimeStart() {
		return joinTimeStart;
	}

	public void setJoinTimeStart(String joinTimeStart) {
		this.joinTimeStart = joinTimeStart;
	}

	public String getJoinTimeEnd() {
		return joinTimeEnd;
	}

	public void setJoinTimeEnd(String joinTimeEnd) {
		this.joinTimeEnd = joinTimeEnd;
	}

	public String getAccedeOrderId() {
		return accedeOrderId;
	}

	public void setAccedeOrderId(String accedeOrderId) {
		this.accedeOrderId = accedeOrderId;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getRepayType() {
		return repayType;
	}

	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}

	public String getPlanOrderId() {
		return planOrderId;
	}

	public void setPlanOrderId(String planOrderId) {
		this.planOrderId = planOrderId;
	}

	public String getRepayStatus() {
		return repayStatus;
	}

	public void setRepayStatus(String repayStatus) {
		this.repayStatus = repayStatus;
	}

	public String getRepayTimeStart() {
		return repayTimeStart;
	}

	public void setRepayTimeStart(String repayTimeStart) {
		this.repayTimeStart = repayTimeStart;
	}

	public String getRepayTimeEnd() {
		return repayTimeEnd;
	}

	public void setRepayTimeEnd(String repayTimeEnd) {
		this.repayTimeEnd = repayTimeEnd;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getLiquidateFactTimeStart() {
		return liquidateFactTimeStart;
	}

	public void setLiquidateFactTimeStart(String liquidateFactTimeStart) {
		this.liquidateFactTimeStart = liquidateFactTimeStart;
	}

	public String getLiquidateFactTimeEnd() {
		return liquidateFactTimeEnd;
	}

	public void setLiquidateFactTimeEnd(String liquidateFactTimeEnd) {
		this.liquidateFactTimeEnd = liquidateFactTimeEnd;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFullExpireTimeEnd() {
		return fullExpireTimeEnd;
	}

	public void setFullExpireTimeEnd(String fullExpireTimeEnd) {
		this.fullExpireTimeEnd = fullExpireTimeEnd;
	}

	public String getLiquidateShouldTimeEnd() {
		return liquidateShouldTimeEnd;
	}

	public void setLiquidateShouldTimeEnd(String liquidateShouldTimeEnd) {
		this.liquidateShouldTimeEnd = liquidateShouldTimeEnd;
	}

	public String getTimeEndSrch() {
		return timeEndSrch;
	}

	public void setTimeEndSrch(String timeEndSrch) {
		this.timeEndSrch = timeEndSrch;
	}

	public String getRepayTimeLastStart() {
		return repayTimeLastStart;
	}

	public void setRepayTimeLastStart(String repayTimeLastStart) {
		this.repayTimeLastStart = repayTimeLastStart;
	}

	public String getRepayTimeLastEnd() {
		return repayTimeLastEnd;
	}

	public void setRepayTimeLastEnd(String repayTimeLastEnd) {
		this.repayTimeLastEnd = repayTimeLastEnd;
	}

	public String getExpireFairValue() {
		return expireFairValue;
	}

	public void setExpireFairValue(String expireFairValue) {
		this.expireFairValue = expireFairValue;
	}

	public String getOrderStatusSrch() {
		return orderStatusSrch;
	}

	public void setOrderStatusSrch(String orderStatusSrch) {
		this.orderStatusSrch = orderStatusSrch;
	}

	public String getActulRepayTimeStart() {
		return actulRepayTimeStart;
	}

	public void setActulRepayTimeStart(String actulRepayTimeStart) {
		this.actulRepayTimeStart = actulRepayTimeStart;
	}

	public String getActulRepayTimeEnd() {
		return actulRepayTimeEnd;
	}

	public void setActulRepayTimeEnd(String actulRepayTimeEnd) {
		this.actulRepayTimeEnd = actulRepayTimeEnd;
	}

	/**
	 * sumAccedeAccount
	 * @return the sumAccedeAccount
	 */
		
	public BigDecimal getSumAccedeAccount() {
		return sumAccedeAccount;
			
	}

	/**
	 * @param sumAccedeAccount the sumAccedeAccount to set
	 */
		
	public void setSumAccedeAccount(BigDecimal sumAccedeAccount) {
		this.sumAccedeAccount = sumAccedeAccount;
			
	}

	/**
	 * sumRepayInterest
	 * @return the sumRepayInterest
	 */
		
	public BigDecimal getSumRepayInterest() {
		return sumRepayInterest;
			
	}

	/**
	 * @param sumRepayInterest the sumRepayInterest to set
	 */
		
	public void setSumRepayInterest(BigDecimal sumRepayInterest) {
		this.sumRepayInterest = sumRepayInterest;
			
	}

	/**
	 * sumRepayAlready
	 * @return the sumRepayAlready
	 */
		
	public String getSumRepayAlready() {
		return sumRepayAlready;
			
	}

	/**
	 * @param sumRepayAlready the sumRepayAlready to set
	 */
		
	public void setSumRepayAlready(String sumRepayAlready) {
		this.sumRepayAlready = sumRepayAlready;
			
	}

	/**
	 * sumRepayWait
	 * @return the sumRepayWait
	 */
		
	public BigDecimal getSumRepayWait() {
		return sumRepayWait;
			
	}

	/**
	 * @param sumRepayWait the sumRepayWait to set
	 */
		
	public void setSumRepayWait(BigDecimal sumRepayWait) {
		this.sumRepayWait = sumRepayWait;
			
	}

	/**
	 * sumPlanRepayCapital
	 * @return the sumPlanRepayCapital
	 */
		
	public String getSumPlanRepayCapital() {
		return sumPlanRepayCapital;
			
	}

	/**
	 * @param sumPlanRepayCapital the sumPlanRepayCapital to set
	 */
		
	public void setSumPlanRepayCapital(String sumPlanRepayCapital) {
		this.sumPlanRepayCapital = sumPlanRepayCapital;
			
	}

	/**
	 * sumPlanRepayInterest
	 * @return the sumPlanRepayInterest
	 */
		
	public String getSumPlanRepayInterest() {
		return sumPlanRepayInterest;
			
	}

	/**
	 * @param sumPlanRepayInterest the sumPlanRepayInterest to set
	 */
		
	public void setSumPlanRepayInterest(String sumPlanRepayInterest) {
		this.sumPlanRepayInterest = sumPlanRepayInterest;
			
	}

	public String getDebtLockPeriodSrch() {
		return debtLockPeriodSrch;
	}

	public void setDebtLockPeriodSrch(String debtLockPeriodSrch) {
		this.debtLockPeriodSrch = debtLockPeriodSrch;
	}

	public String getBorrowStyleSrch() {
		return borrowStyleSrch;
	}

	public void setBorrowStyleSrch(String borrowStyleSrch) {
		this.borrowStyleSrch = borrowStyleSrch;
	}

	public String getRefereeNameSrch() {
		return refereeNameSrch;
	}

	public void setRefereeNameSrch(String refereeNameSrch) {
		this.refereeNameSrch = refereeNameSrch;
	}
}
