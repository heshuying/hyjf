package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;
import java.math.BigDecimal;

public class AdminActivityReturncashCustomize implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String userId;
	
	private String username;
	
	private String truename;
	
	private String mobile;
	
	private String referrerUserName;
	
	private String attribute;
	
	private BigDecimal investTotalActivity;
	
	private String hasLostreward;
	
	private String orderId;
	
	private BigDecimal rewardTotal;
	
	private String userType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public String getReferrerUserName() {
		return referrerUserName;
	}

	public void setReferrerUserName(String referrerUserName) {
		this.referrerUserName = referrerUserName;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public BigDecimal getInvestTotalActivity() {
		return investTotalActivity;
	}

	public void setInvestTotalActivity(BigDecimal investTotalActivity) {
		this.investTotalActivity = investTotalActivity;
	}

	public String getHasLostreward() {
		return hasLostreward;
	}

	public void setHasLostreward(String hasLostreward) {
		this.hasLostreward = hasLostreward;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getRewardTotal() {
		return rewardTotal;
	}

	public void setRewardTotal(BigDecimal rewardTotal) {
		this.rewardTotal = rewardTotal;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}


	
	
	
}