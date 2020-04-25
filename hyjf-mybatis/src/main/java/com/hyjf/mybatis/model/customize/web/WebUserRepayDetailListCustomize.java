/**
 * Description:用户开户列表前端显示查询所用po
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize.web;

/**
 * @author 王坤
 */

public class WebUserRepayDetailListCustomize {

	// 用户id
	public String userId;
	// 用户名
	public String userName;
	// 用户出借单号
	public String nid;
	// 应还本金
	public String repayAccount;
	// 应还利息
	public String repayInterest;
	// 项目管理费
	public String financeManage;
	// 提前天数
	public String advanceDays;
	// 提前还款利息
	public String advanceInterest;
	// 延期天数
	public String delayDays;
	// 延期利息
	public String delayInterest;
	// 逾期天数
	public String lateDays;
	// 逾期罚息
	public String lateInterest;
	// 还款时间
	public String repayTime;
	// 实际还款
	public String repayTotal;
	// 还款状态
	public String status;

	/**
	 * 构造方法
	 */
	public WebUserRepayDetailListCustomize() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRepayAccount() {
		return repayAccount;
	}

	public void setRepayAccount(String repayAccount) {
		this.repayAccount = repayAccount;
	}

	public String getRepayInterest() {
		return repayInterest;
	}

	public void setRepayInterest(String repayInterest) {
		this.repayInterest = repayInterest;
	}

	public String getFinanceManage() {
		return financeManage;
	}

	public void setFinanceManage(String financeManage) {
		this.financeManage = financeManage;
	}

	public String getAdvanceDays() {
		return advanceDays;
	}

	public void setAdvanceDays(String advanceDays) {
		this.advanceDays = advanceDays;
	}

	public String getAdvanceInterest() {
		return advanceInterest;
	}

	public void setAdvanceInterest(String advanceInterest) {
		this.advanceInterest = advanceInterest;
	}

	public String getDelayDays() {
		return delayDays;
	}

	public void setDelayDays(String delayDays) {
		this.delayDays = delayDays;
	}

	public String getDelayInterest() {
		return delayInterest;
	}

	public void setDelayInterest(String delayInterest) {
		this.delayInterest = delayInterest;
	}

	public String getLateDays() {
		return lateDays;
	}

	public void setLateDays(String lateDays) {
		this.lateDays = lateDays;
	}

	public String getLateInterest() {
		return lateInterest;
	}

	public void setLateInterest(String lateInterest) {
		this.lateInterest = lateInterest;
	}

	public String getRepayTime() {
		return repayTime;
	}

	public void setRepayTime(String repayTime) {
		this.repayTime = repayTime;
	}

	public String getRepayTotal() {
		return repayTotal;
	}

	public void setRepayTotal(String repayTotal) {
		this.repayTotal = repayTotal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

}
