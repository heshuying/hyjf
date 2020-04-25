package com.hyjf.batch.result.account;

public class UserAccountResultBean {
	/**
	 * 电子账号
	 */
	private String accountId;
	/**
	 * 证件编号
	 */
	private String idCard;
	/**
	 * 证件类型
	 */
	private String idType;
	/**
	 * 成功失败标识
	 */
	private String flag;
	/**
	 * 失败错误码
	 */
	private String errCode;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 账户类型
	 */
	private String accType;
	/**
	 * 请求方用户id
	 */
	private String appId;
	/**
	 * 手机号码
	 */
	private String mobile;
	/**
	 * 请求方保留信息
	 */
	private String info;
	/**
	 * 保留域
	 */
	private String revers;
	
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getRevers() {
		return revers;
	}
	public void setRevers(String revers) {
		this.revers = revers;
	}
	
}
