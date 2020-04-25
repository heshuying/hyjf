package com.hyjf.mybatis.model.customize.admin.act;

public class ActTen2017Customize {
	private Integer userId;
	
	private String userName;
	
	private String trueName;
	
	private String mobile;
	
	/**
	 * 出借金额
	 */
	private String tenderAccount;
	
	/**
	 * 出借 时间
	 */
	private String tenderTime;
	
	/**
	 * 奖励名称
	 */
	private String rewardName;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTenderAccount() {
		return tenderAccount;
	}

	public void setTenderAccount(String tenderAccount) {
		this.tenderAccount = tenderAccount;
	}

	public String getTenderTime() {
		return tenderTime;
	}

	public void setTenderTime(String tenderTime) {
		this.tenderTime = tenderTime;
	}

	public String getRewardName() {
		return rewardName;
	}

	public void setRewardName(String rewardName) {
		this.rewardName = rewardName;
	}
	
	
}
