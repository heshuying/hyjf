/**
 * Description: 汇计划综合类，列表，加入明细，还款相关字段
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: LIBIN
 * @version: 1.0
 * Created at: 2017年08月11日
 * Modification History:
 */

package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

public class PlanListCommonCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1204729862333661633L;
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
	 * 检索条件 出借状态 0 全部；1 启用；2 关闭'
	 */
	private String planStatusSrch;
	/**
	 * 检索条件 显示状态 0 全部；1 启用；2 关闭'
	 */
	private String planDisplayStatusSrch;
	public String getPlanDisplayStatusSrch() {
		return planDisplayStatusSrch;
	}
	public void setPlanDisplayStatusSrch(String planDisplayStatusSrch) {
		this.planDisplayStatusSrch = planDisplayStatusSrch;
	}
	/**
	 * 检索条件锁定期
	 */
	private String lockPeriodSrch;
	/**
	 * 检索条件 添加时间开始
	 */
	private String addTimeStart;

	/**
	 * 检索条件 添加时间结束
	 */
	private String addTimeEnd;
	
	/*之下暂时不用*/
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
	 * 检索条件 plan表状态 0 发起中；1 待审核；2审核不通过；3待开放；4募集中；5锁定中；6清算中；7清算完成，8未还款，9还款中，10还款完成
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
	 * 检索条件 还款方式 汇计划三期新增
	 */
	private String borrowStyleSrch;
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
	public String getPlanStatusSrch() {
		return planStatusSrch;
	}
	public void setPlanStatusSrch(String planStatusSrch) {
		this.planStatusSrch = planStatusSrch;
	}
	public String getFullExpireTime() {
		return fullExpireTime;
	}
	public void setFullExpireTime(String fullExpireTime) {
		this.fullExpireTime = fullExpireTime;
	}
	public String getFullExpireTimeEnd() {
		return fullExpireTimeEnd;
	}
	public void setFullExpireTimeEnd(String fullExpireTimeEnd) {
		this.fullExpireTimeEnd = fullExpireTimeEnd;
	}
	public String getLiquidateFactTime() {
		return liquidateFactTime;
	}
	public void setLiquidateFactTime(String liquidateFactTime) {
		this.liquidateFactTime = liquidateFactTime;
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
	public String getLiquidateShouldTime() {
		return liquidateShouldTime;
	}
	public void setLiquidateShouldTime(String liquidateShouldTime) {
		this.liquidateShouldTime = liquidateShouldTime;
	}
	public String getLiquidateShouldTimeEnd() {
		return liquidateShouldTimeEnd;
	}
	public void setLiquidateShouldTimeEnd(String liquidateShouldTimeEnd) {
		this.liquidateShouldTimeEnd = liquidateShouldTimeEnd;
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
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	public String getLockPeriodSrch() {
		return lockPeriodSrch;
	}
	public void setLockPeriodSrch(String lockPeriodSrch) {
		this.lockPeriodSrch = lockPeriodSrch;
	}
	public String getAddTimeStart() {
		return addTimeStart;
	}
	public void setAddTimeStart(String addTimeStart) {
		this.addTimeStart = addTimeStart;
	}
	public String getAddTimeEnd() {
		return addTimeEnd;
	}
	public void setAddTimeEnd(String addTimeEnd) {
		this.addTimeEnd = addTimeEnd;
	}
	public String getBorrowStyleSrch() {
		return borrowStyleSrch;
	}
	public void setBorrowStyleSrch(String borrowStyleSrch) {
		this.borrowStyleSrch = borrowStyleSrch;
	}
}
