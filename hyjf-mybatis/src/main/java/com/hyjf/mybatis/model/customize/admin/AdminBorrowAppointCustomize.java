package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;

public class AdminBorrowAppointCustomize implements Serializable {
	
    /**
	 * serialVersionUID:序列化id
	 */
		
	private static final long serialVersionUID = 4264420447504028078L;

	private String id;

    private String userName;

    private String orderId;

    private String tenderNid;

    private String borrowNid;

    private String borrowPeriod;

    private String borrowApr;

    private String borrowAccount;

    private String account;

    private String appointStatus;
    
    private String appointStatusInfo;

    private String appointTime;

    private String appointRemark;

    private String cancelTime;

    private String tenderStatus;
    
    private String tenderStatusInfo;

    private String tenderTime;

    private String tenderRemark;
    
    private String verifyTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTenderNid() {
		return tenderNid;
	}

	public void setTenderNid(String tenderNid) {
		this.tenderNid = tenderNid;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public String getBorrowPeriod() {
		return borrowPeriod;
	}

	public void setBorrowPeriod(String borrowPeriod) {
		this.borrowPeriod = borrowPeriod;
	}

	public String getBorrowApr() {
		return borrowApr;
	}

	public void setBorrowApr(String borrowApr) {
		this.borrowApr = borrowApr;
	}

	public String getBorrowAccount() {
		return borrowAccount;
	}

	public void setBorrowAccount(String borrowAccount) {
		this.borrowAccount = borrowAccount;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAppointStatus() {
		return appointStatus;
	}

	public void setAppointStatus(String appointStatus) {
		this.appointStatus = appointStatus;
	}

	public String getAppointTime() {
		return appointTime;
	}

	public void setAppointTime(String appointTime) {
		this.appointTime = appointTime;
	}

	public String getAppointRemark() {
		return appointRemark;
	}

	public void setAppointRemark(String appointRemark) {
		this.appointRemark = appointRemark;
	}

	public String getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}

	public String getTenderStatus() {
		return tenderStatus;
	}

	public void setTenderStatus(String tenderStatus) {
		this.tenderStatus = tenderStatus;
	}

	public String getTenderTime() {
		return tenderTime;
	}

	public void setTenderTime(String tenderTime) {
		this.tenderTime = tenderTime;
	}

	public String getTenderRemark() {
		return tenderRemark;
	}

	public void setTenderRemark(String tenderRemark) {
		this.tenderRemark = tenderRemark;
	}

	public String getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(String verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getAppointStatusInfo() {
		return appointStatusInfo;
	}

	public void setAppointStatusInfo(String appointStatusInfo) {
		this.appointStatusInfo = appointStatusInfo;
	}

	public String getTenderStatusInfo() {
		return tenderStatusInfo;
	}

	public void setTenderStatusInfo(String tenderStatusInfo) {
		this.tenderStatusInfo = tenderStatusInfo;
	}

}