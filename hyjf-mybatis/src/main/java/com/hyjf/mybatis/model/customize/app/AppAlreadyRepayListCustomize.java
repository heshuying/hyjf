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

package com.hyjf.mybatis.model.customize.app;

import java.io.Serializable;

public class AppAlreadyRepayListCustomize implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 3309762895449949778L;
	// 出借中项目id
	private String borrowNid;
	// 出借中项目名称
	private String borrowName;
	// 项目年华收益
	private String borrowApr;
	// 出借中待收本金
	private String account;
	// 出借中待收收益
	private String interest;
	// 出借合同url
	private String borrowUrl = "";
	// 项目进度
	private String borrowSchedule;
	// 优惠券出借时的优惠券类型，费优惠券出借则为空字符串
	private String couponType;
	private String label;
	// 出借类型 1直投 2优惠券 3加息
	private String investType;
	// RTB
	private String projectType;
	// 回款时间
	private String recoverTime;

	// 出借订单号
	private String orderId;
	// 项目期限
	private String period;
	//加息收益率
	private String borrowExtraYield;
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRecoverTime() {
		return recoverTime;
	}

	public void setRecoverTime(String recoverTime) {
		this.recoverTime = recoverTime;
	}

	/**
	 * 构造方法
	 */

	public AppAlreadyRepayListCustomize() {
		super();
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public String getBorrowName() {
		return borrowName;
	}

	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getBorrowApr() {
		return borrowApr;
	}

	public void setBorrowApr(String borrowApr) {
		this.borrowApr = borrowApr;
	}

	public String getBorrowUrl() {
		return borrowUrl;
	}

	public void setBorrowUrl(String borrowUrl) {
		this.borrowUrl = borrowUrl;
	}

	public String getBorrowSchedule() {
		return borrowSchedule;
	}

	public void setBorrowSchedule(String borrowSchedule) {
		this.borrowSchedule = borrowSchedule;
	}


	public String getInvestType() {
		return investType;
	}

	public void setInvestType(String investType) {
		this.investType = investType;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getBorrowExtraYield() { return borrowExtraYield; }

	public void setBorrowExtraYield(String borrowExtraYield) { this.borrowExtraYield = borrowExtraYield; }

}
