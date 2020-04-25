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

package com.hyjf.mybatis.model.customize.web.htj;

import java.io.Serializable;

/**
 * @author 王坤
 */

public class DebtPlanBorrowDetailCustomize implements Serializable {

	/** 序列化id */
	private static final long serialVersionUID = 4853144718353886143L;
	/* 项目大分类 projectType HZT 汇直投 HXF 汇消费 */
	private String projectType;
	/* 项目子分类代码 type 0汇保贷 1汇典贷 2汇小贷 3汇车贷 4新手标 5汇租赁 6供应贷 7汇房贷 8汇消费 9汇资产 */
	private String type;
	/* 项目子分类名称 typeName 0汇保贷 1汇典贷 2汇小贷 3汇车贷 4新手标 5汇租赁 6供应贷 7汇房贷 8汇消费 9汇资产 */
	private String typeName;
	/* 项目编号 borrowNid */
	private String borrowNid;
	/* 项目名称 borrowName */
	private String borrowName;
	/* 项目还款方式 */
	private String borrowStyle;
	/* 授信额度 userCredit */
	private String userCredit;
	/* 合作机构MeasuresInstit */
	private String measuresInstit;
	/* 授信额度 userCredit */
	private String borrowAccount;
	/* 项目年化收益 borrowApr */
	private String borrowApr;
	/* 项目期限 borrowPeriod */
	private String borrowPeriod;
	/* 项目期限类型 borrowPeriodType */
	private String borrowPeriodType;
	/* 可投金额 investAccount */
	private String investAccount;
	/* 项目区分 comOrPer 项目是个人项目还是企业项目 1企业 2个人 */
	private String comOrPer;
	/* 预期收益 borrowInterest */
	private String borrowInterest;
	/* 还款方式 repayStyle */
	private String repayStyle;
	/* 项目进度 borrowSchedule */
	private String borrowSchedule;
	// 最小出借金额
	private String tenderAccountMin;
	// 最大出借金额
	private String tenderAccountMax;
	/* 发标时间 sendTime ----------- */
	private String sendTime;
	// 项目满标时间
	private String fullTime;
	// 项目结束时间
	private String endTime;
	/* 定时发标时间 sendTime ----------- */
	private String onTime;
	/* 定时发标时间戳 time ----------- */
	private String time;
	/* 项目状态 status 10 定时发标 11出借中 12复审中 13 还款中 14 已还款 15 已流标 */
	private String status;
	/* 倍增金额 increaseMoney ----------- */
	private String increaseMoney;
	/* 加息券 interestCoupon ----------- */
	private String interestCoupon;
	/* 体验金 tasteMoney ----------- */
	private String tasteMoney;
	/* 预约状态 0初始 1预约中 2预约结束 bookingStatus ----------- */
	private String bookingStatus;
	/* 预约开始时间 */
	private Integer bookingBeginTime;
	/* 预约结束时间 ----------- */
	private Integer bookingEndTime;
	/* 预约标等待预约金额 borrowAccountWaitAppoint ----------- */
	private String borrowAccountWaitAppoint;
	/* 预约进度 borrowAccountScaleAppoint ----------- */
	private String borrowAccountScaleAppoint;
	/* 开始预约时间格式化显示 onAppointTime ----------- */
	private String onAppointTime;
	/* 信用评级 ----------- */
	private String borrowLevel;

	public DebtPlanBorrowDetailCustomize() {
		super();
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getBorrowName() {
		return borrowName;
	}

	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public String getUserCredit() {
		return userCredit;
	}

	public void setUserCredit(String userCredit) {
		this.userCredit = userCredit;
	}

	public String getRepayStyle() {
		return repayStyle;
	}

	public void setRepayStyle(String repayStyle) {
		this.repayStyle = repayStyle;
	}

	public String getBorrowApr() {
		return borrowApr;
	}

	public void setBorrowApr(String borrowApr) {
		this.borrowApr = borrowApr;
	}

	public String getBorrowPeriod() {
		return borrowPeriod;
	}

	public void setBorrowPeriod(String borrowPeriod) {
		this.borrowPeriod = borrowPeriod;
	}

	public String getInvestAccount() {
		return investAccount;
	}

	public void setInvestAccount(String investAccount) {
		this.investAccount = investAccount;
	}

	public String getBorrowSchedule() {
		return borrowSchedule;
	}

	public void setBorrowSchedule(String borrowSchedule) {
		this.borrowSchedule = borrowSchedule;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComOrPer() {
		return comOrPer;
	}

	public void setComOrPer(String comOrPer) {
		this.comOrPer = comOrPer;
	}

	public String getBorrowAccount() {
		return borrowAccount;
	}

	public void setBorrowAccount(String borrowAccount) {
		this.borrowAccount = borrowAccount;
	}

	public String getBorrowInterest() {
		return borrowInterest;
	}

	public void setBorrowInterest(String borrowInterest) {
		this.borrowInterest = borrowInterest;
	}

	public String getMeasuresInstit() {
		return measuresInstit;
	}

	public void setMeasuresInstit(String measuresInstit) {
		this.measuresInstit = measuresInstit;
	}

	public String getBorrowStyle() {
		return borrowStyle;
	}

	public void setBorrowStyle(String borrowStyle) {
		this.borrowStyle = borrowStyle;
	}

	public String getBorrowPeriodType() {
		return borrowPeriodType;
	}

	public void setBorrowPeriodType(String borrowPeriodType) {
		this.borrowPeriodType = borrowPeriodType;
	}

	public String getTenderAccountMin() {
		return tenderAccountMin;
	}

	public void setTenderAccountMin(String tenderAccountMin) {
		this.tenderAccountMin = tenderAccountMin;
	}

	public String getTenderAccountMax() {
		return tenderAccountMax;
	}

	public void setTenderAccountMax(String tenderAccountMax) {
		this.tenderAccountMax = tenderAccountMax;
	}

	public String getOnTime() {
		return onTime;
	}

	public void setOnTime(String onTime) {
		this.onTime = onTime;
	}

	public String getFullTime() {
		return fullTime;
	}

	public void setFullTime(String fullTime) {
		this.fullTime = fullTime;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getIncreaseMoney() {
		return increaseMoney;
	}

	public void setIncreaseMoney(String increaseMoney) {
		this.increaseMoney = increaseMoney;
	}

	public String getInterestCoupon() {
		return interestCoupon;
	}

	public void setInterestCoupon(String interestCoupon) {
		this.interestCoupon = interestCoupon;
	}

	public String getTasteMoney() {
		return tasteMoney;
	}

	public void setTasteMoney(String tasteMoney) {
		this.tasteMoney = tasteMoney;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public String getBorrowAccountWaitAppoint() {
		return borrowAccountWaitAppoint;
	}

	public void setBorrowAccountWaitAppoint(String borrowAccountWaitAppoint) {
		this.borrowAccountWaitAppoint = borrowAccountWaitAppoint;
	}

	public String getBorrowAccountScaleAppoint() {
		return borrowAccountScaleAppoint;
	}

	public void setBorrowAccountScaleAppoint(String borrowAccountScaleAppoint) {
		this.borrowAccountScaleAppoint = borrowAccountScaleAppoint;
	}

	public Integer getBookingBeginTime() {
		return bookingBeginTime;
	}

	public void setBookingBeginTime(Integer bookingBeginTime) {
		this.bookingBeginTime = bookingBeginTime;
	}

	public Integer getBookingEndTime() {
		return bookingEndTime;
	}

	public void setBookingEndTime(Integer bookingEndTime) {
		this.bookingEndTime = bookingEndTime;
	}

	public String getOnAppointTime() {
		return onAppointTime;
	}

	public void setOnAppointTime(String onAppointTime) {
		this.onAppointTime = onAppointTime;
	}

	public String getBorrowLevel() {
		return borrowLevel;
	}

	public void setBorrowLevel(String borrowLevel) {
		this.borrowLevel = borrowLevel;
	}

}
