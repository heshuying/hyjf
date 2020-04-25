package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.math.BigDecimal;

public class SqlWithdrawCustomize implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//huiyingdai_account_withdraw
	private int id;

	private Integer userId;

	private String nid;

	private int status;

	private String account;

	private String bank;

	private int bankId;

	private String branch;

	private int province;

	private int city;

	private BigDecimal total;

	private BigDecimal credited;

	private String fee;

	private int verifyUserid;

	private int verifyTime;

	private String verifyRemark;

	private String addtime;

	private String addip;

	private String remark;

	private int updateTime;

	private int client;

	private int isok;

	private String reason;

	private int activityFlag;
	
	//oa_department
	private String neme;
	private String firstdepartname;
	private String seconddepartname;
		
	//huiyingdai_users
	private String username;
		
	//oa_users
	private String user_realname;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getCredited() {
		return credited;
	}

	public void setCredited(BigDecimal credited) {
		this.credited = credited;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public int getVerifyUserid() {
		return verifyUserid;
	}

	public void setVerifyUserid(int verifyUserid) {
		this.verifyUserid = verifyUserid;
	}

	public int getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(int verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getVerifyRemark() {
		return verifyRemark;
	}

	public void setVerifyRemark(String verifyRemark) {
		this.verifyRemark = verifyRemark;
	}
	
	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getAddip() {
		return addip;
	}

	public void setAddip(String addip) {
		this.addip = addip;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(int updateTime) {
		this.updateTime = updateTime;
	}

	public int getClient() {
		return client;
	}

	public void setClient(int client) {
		this.client = client;
	}

	public int getIsok() {
		return isok;
	}

	public void setIsok(int isok) {
		this.isok = isok;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getActivityFlag() {
		return activityFlag;
	}

	public void setActivityFlag(int activityFlag) {
		this.activityFlag = activityFlag;
	}

	public String getNeme() {
		return neme;
	}

	public void setNeme(String neme) {
		this.neme = neme;
	}

	public String getFirstdepartname() {
		return firstdepartname;
	}

	public void setFirstdepartname(String firstdepartname) {
		this.firstdepartname = firstdepartname;
	}

	public String getSeconddepartname() {
		return seconddepartname;
	}

	public void setSeconddepartname(String seconddepartname) {
		this.seconddepartname = seconddepartname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUser_realname() {
		return user_realname;
	}

	public void setUser_realname(String user_realname) {
		this.user_realname = user_realname;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	 
	
	 
	 

}
