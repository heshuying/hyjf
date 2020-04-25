package com.hyjf.web.user.invite;

import java.math.BigDecimal;

public class InviteRecordBean {

	public Integer userId;
	public String username;
	/**
	 * 邀请时间
	 */
	public String inviteTime;

	public String userStatus;
	
	public BigDecimal account;

	public BigDecimal recentWait;
	
	public String recentWaitTime;
	
	public BigDecimal balance;//可出借金额

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getInviteTime() {
		return inviteTime;
	}

	public void setInviteTime(String inviteTime) {
		this.inviteTime = inviteTime;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public BigDecimal getAccount() {
		return account;
	}

	public void setAccount(BigDecimal account) {
		this.account = account;
	}

	public BigDecimal getRecentWait() {
		return recentWait;
	}

	public void setRecentWait(BigDecimal recentWait) {
		this.recentWait = recentWait;
	}

	public String getRecentWaitTime() {
		return recentWaitTime;
	}

	public void setRecentWaitTime(String recentWaitTime) {
		this.recentWaitTime = recentWaitTime;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
}
