package com.hyjf.mybatis.model.customize.web.hjh;

import java.io.Serializable;

/**
 * 
 * @author HBZ
 */
public class HjhReInvestDebtCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1421679819371458772L;

	private int limitStart = -1;

	private int limitEnd = -1;

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

	//计划订单号（检索）
	private String assignPlanOrderIdSrch;
	//承接计划编号（检索）
	private String assignPlanNidSrch;
	//承接订单号（检索）
	private String assignOrderIdSrch;
	//承接人（检索）
	private String userNameSrch;
	//出让人（检索）
	private String creditUserNameSrch;
	//债转编号（检索）
	private String creditNidSrch;
	//原项目编号（检索）
	private String borrowNidSrch;
	//承接方式（检索）
	private String assignTypeSrch;
	//还款方式（检索）
	private String borrowStyleSrch;
	
	//计划订单号
	private String assignPlanOrderId;
	//承接计划编号
	private String assignPlanNid;
	//承接订单号
	private String assignOrderId;
	//承接人
	private String userName;
	//出让人
	private String creditUserName;
	//债转编号
	private String creditNid;
	//原项目编号
	private String borrowNid;
	//还款方式
	private String borrowStyle;
	//承接本金
	private String assignCapital;
	//垫付利息
	private String assignInterestAdvance;
	//实际支付金额(购买价格)
	private String assignPay;
	//承接方式
	private String assignType;
	//项目总期数
	private String borrowPeriod;
	//承接时所在期数
	private String assignPeriod;
	//承接时间
	private String assignTime;
	//前画面传入
	private String planNid;
	private String date;

	/**
	 * assignPlanNidSrch
	 * @return the assignPlanNidSrch
	 */
	
	public String getAssignPlanNidSrch() {
		return assignPlanNidSrch;
	}

	/**
	 * @param assignPlanNidSrch the assignPlanNidSrch to set
	 */
	
	public void setAssignPlanNidSrch(String assignPlanNidSrch) {
		this.assignPlanNidSrch = assignPlanNidSrch;
	}

	/**
	 * assignPlanOrderIdSrch
	 * @return the assignPlanOrderIdSrch
	 */
	
	public String getAssignPlanOrderIdSrch() {
		return assignPlanOrderIdSrch;
	}

	/**
	 * @param assignPlanOrderIdSrch the assignPlanOrderIdSrch to set
	 */
	
	public void setAssignPlanOrderIdSrch(String assignPlanOrderIdSrch) {
		this.assignPlanOrderIdSrch = assignPlanOrderIdSrch;
	}

	/**
	 * userNameSrch
	 * @return the userNameSrch
	 */
	
	public String getUserNameSrch() {
		return userNameSrch;
	}

	/**
	 * @param userNameSrch the userNameSrch to set
	 */
	
	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}

	/**
	 * creditUserNameSrch
	 * @return the creditUserNameSrch
	 */
	
	public String getCreditUserNameSrch() {
		return creditUserNameSrch;
	}

	/**
	 * @param creditUserNameSrch the creditUserNameSrch to set
	 */
	
	public void setCreditUserNameSrch(String creditUserNameSrch) {
		this.creditUserNameSrch = creditUserNameSrch;
	}

	/**
	 * creditNidSrch
	 * @return the creditNidSrch
	 */
	
	public String getCreditNidSrch() {
		return creditNidSrch;
	}

	/**
	 * @param creditNidSrch the creditNidSrch to set
	 */
	
	public void setCreditNidSrch(String creditNidSrch) {
		this.creditNidSrch = creditNidSrch;
	}

	/**
	 * borrowNidSrch
	 * @return the borrowNidSrch
	 */
	
	public String getBorrowNidSrch() {
		return borrowNidSrch;
	}

	/**
	 * @param borrowNidSrch the borrowNidSrch to set
	 */
	
	public void setBorrowNidSrch(String borrowNidSrch) {
		this.borrowNidSrch = borrowNidSrch;
	}

	/**
	 * assignTypeSrch
	 * @return the assignTypeSrch
	 */
	
	public String getAssignTypeSrch() {
		return assignTypeSrch;
	}

	/**
	 * @param assignTypeSrch the assignTypeSrch to set
	 */
	
	public void setAssignTypeSrch(String assignTypeSrch) {
		this.assignTypeSrch = assignTypeSrch;
	}

	/**
	 * borrowStyleSrch
	 * @return the borrowStyleSrch
	 */
	
	public String getBorrowStyleSrch() {
		return borrowStyleSrch;
	}

	/**
	 * @param borrowStyleSrch the borrowStyleSrch to set
	 */
	
	public void setBorrowStyleSrch(String borrowStyleSrch) {
		this.borrowStyleSrch = borrowStyleSrch;
	}

	/**
	 * planNid
	 * @return the planNid
	 */
	
	public String getPlanNid() {
		return planNid;
	}

	/**
	 * @param planNid the planNid to set
	 */
	
	public void setPlanNid(String planNid) {
		this.planNid = planNid;
	}

	/**
	 * date
	 * @return the date
	 */
	
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * assignPlanNid
	 * @return the assignPlanNid
	 */
	
	public String getAssignPlanNid() {
		return assignPlanNid;
	}

	/**
	 * @param assignPlanNid the assignPlanNid to set
	 */
	
	public void setAssignPlanNid(String assignPlanNid) {
		this.assignPlanNid = assignPlanNid;
	}

	/**
	 * assignPlanOrderId
	 * @return the assignPlanOrderId
	 */
	
	public String getAssignPlanOrderId() {
		return assignPlanOrderId;
	}

	/**
	 * @param assignPlanOrderId the assignPlanOrderId to set
	 */
	
	public void setAssignPlanOrderId(String assignPlanOrderId) {
		this.assignPlanOrderId = assignPlanOrderId;
	}

	/**
	 * userName
	 * @return the userName
	 */
	
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * creditUserName
	 * @return the creditUserName
	 */
	
	public String getCreditUserName() {
		return creditUserName;
	}

	/**
	 * @param creditUserName the creditUserName to set
	 */
	
	public void setCreditUserName(String creditUserName) {
		this.creditUserName = creditUserName;
	}

	/**
	 * creditNid
	 * @return the creditNid
	 */
	
	public String getCreditNid() {
		return creditNid;
	}

	/**
	 * @param creditNid the creditNid to set
	 */
	
	public void setCreditNid(String creditNid) {
		this.creditNid = creditNid;
	}

	/**
	 * borrowNid
	 * @return the borrowNid
	 */
	
	public String getBorrowNid() {
		return borrowNid;
	}

	/**
	 * @param borrowNid the borrowNid to set
	 */
	
	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	/**
	 * borrowStyle
	 * @return the borrowStyle
	 */
	
	public String getBorrowStyle() {
		return borrowStyle;
	}

	/**
	 * @param borrowStyle the borrowStyle to set
	 */
	
	public void setBorrowStyle(String borrowStyle) {
		this.borrowStyle = borrowStyle;
	}

	/**
	 * assignCapital
	 * @return the assignCapital
	 */
	
	public String getAssignCapital() {
		return assignCapital;
	}

	/**
	 * @param assignCapital the assignCapital to set
	 */
	
	public void setAssignCapital(String assignCapital) {
		this.assignCapital = assignCapital;
	}

	/**
	 * assignInterestAdvance
	 * @return the assignInterestAdvance
	 */
	
	public String getAssignInterestAdvance() {
		return assignInterestAdvance;
	}

	/**
	 * @param assignInterestAdvance the assignInterestAdvance to set
	 */
	
	public void setAssignInterestAdvance(String assignInterestAdvance) {
		this.assignInterestAdvance = assignInterestAdvance;
	}

	/**
	 * assignPay
	 * @return the assignPay
	 */
	
	public String getAssignPay() {
		return assignPay;
	}

	/**
	 * @param assignPay the assignPay to set
	 */
	
	public void setAssignPay(String assignPay) {
		this.assignPay = assignPay;
	}

	/**
	 * assignType
	 * @return the assignType
	 */
	
	public String getAssignType() {
		return assignType;
	}

	/**
	 * @param assignType the assignType to set
	 */
	
	public void setAssignType(String assignType) {
		this.assignType = assignType;
	}

	/**
	 * borrowPeriod
	 * @return the borrowPeriod
	 */
	
	public String getBorrowPeriod() {
		return borrowPeriod;
	}

	/**
	 * @param borrowPeriod the borrowPeriod to set
	 */
	
	public void setBorrowPeriod(String borrowPeriod) {
		this.borrowPeriod = borrowPeriod;
	}

	/**
	 * assignPeriod
	 * @return the assignPeriod
	 */
	
	public String getAssignPeriod() {
		return assignPeriod;
	}

	/**
	 * @param assignPeriod the assignPeriod to set
	 */
	
	public void setAssignPeriod(String assignPeriod) {
		this.assignPeriod = assignPeriod;
	}

	/**
	 * assignTime
	 * @return the assignTime
	 */
	
	public String getAssignTime() {
		return assignTime;
	}

	/**
	 * @param assignTime the assignTime to set
	 */
	
	public void setAssignTime(String assignTime) {
		this.assignTime = assignTime;
	}

	/**
	 * assignOrderId
	 * @return the assignOrderId
	 */
		
	public String getAssignOrderId() {
		return assignOrderId;
			
	}

	/**
	 * @param assignOrderId the assignOrderId to set
	 */
		
	public void setAssignOrderId(String assignOrderId) {
		this.assignOrderId = assignOrderId;
			
	}

	/**
	 * assignOrderIdSrch
	 * @return the assignOrderIdSrch
	 */
		
	public String getAssignOrderIdSrch() {
		return assignOrderIdSrch;
			
	}

	/**
	 * @param assignOrderIdSrch the assignOrderIdSrch to set
	 */
		
	public void setAssignOrderIdSrch(String assignOrderIdSrch) {
		this.assignOrderIdSrch = assignOrderIdSrch;
			
	}
}
