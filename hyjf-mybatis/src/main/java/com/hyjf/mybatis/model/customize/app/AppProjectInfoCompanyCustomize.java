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

/**
 * @author 王坤
 */

public class AppProjectInfoCompanyCustomize implements Serializable {

	/** 序列化id */
	private static final long serialVersionUID = -2708328205111331639L;
	/* 项目大分类 projectType HZT 汇直投 HXF 汇消费 */
	private String projectType;
	/* 项目子分类代码 type */
	private String type;
	/* 项目子分类名称 typeName */
	private String typeName;
	/* 项目名称 borrowName */
	private String borrowName;
	/* 项目编号 borrowNid */
	private String borrowNid;
	/* 项目总额 account */
	private String account;
	/* 还款方式 repayStyle */
	private String repayStyle;
	/* 项目年化收益 borrowApr */
	private String borrowApr;
	/* 项目期限 borrowPeriod */
	private String borrowPeriod;
	/* 可投金额 investAccount */
	private String investAccount;
	/* 项目进度 borrowSchedule */
	private String borrowSchedule;
	/* 开标时间 onTime */
	private String onTime;
	/* 发标时间 sendTime ----------- */
	private String sendTime;
	/* 满标时间 fullTime ------------ */
	private String fullTime;
	/* 项目状态 status 10 定时发标 11出借中 12复审中 13 还款中 14 已还款 15 已流标 */
	private String status;

	public AppProjectInfoCompanyCustomize() {
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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
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

	public String getOnTime() {
		return onTime;
	}

	public void setOnTime(String onTime) {
		this.onTime = onTime;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getFullTime() {
		return fullTime;
	}

	public void setFullTime(String fullTime) {
		this.fullTime = fullTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
