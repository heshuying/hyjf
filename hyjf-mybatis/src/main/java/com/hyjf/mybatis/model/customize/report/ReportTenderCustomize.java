package com.hyjf.mybatis.model.customize.report;

/**
 * 出借报表
 * @author HBZ
 */
public class ReportTenderCustomize {

	private String regionNameSer; //大区名称
	private String branchNameSer; //分公司名称
	private String startDate; //创建时间 起始
	private String endDate; //创建时间  结束

	private int limitStart = -1;
	private int limitEnd = -1;

	private String regionName; //大区名称
	private String branchName; //分公司名称
	private String userId;
	private String userName;
	private String trueName;
	private String referrerName;
	private String tenderMoney; //出借金额
	private String tenderTime;//出借时间
	
	
	public String getRegionNameSer() {
		return regionNameSer;
	}
	public void setRegionNameSer(String regionNameSer) {
		this.regionNameSer = regionNameSer;
	}
	public String getBranchNameSer() {
		return branchNameSer;
	}
	public void setBranchNameSer(String branchNameSer) {
		this.branchNameSer = branchNameSer;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
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
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public String getReferrerName() {
		return referrerName;
	}
	public void setReferrerName(String referrerName) {
		this.referrerName = referrerName;
	}
	public String getTenderMoney() {
		return tenderMoney;
	}
	public void setTenderMoney(String tenderMoney) {
		this.tenderMoney = tenderMoney;
	}
	public String getTenderTime() {
		return tenderTime;
	}
	public void setTenderTime(String tenderTime) {
		this.tenderTime = tenderTime;
	}
	
	
}

	