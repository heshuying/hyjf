package com.hyjf.batch.user.account;

public class UserAccountBean {
	
	/**
	 * 证件编号
	 */
	private String idCard;
	/**
	 * 证件类型 01身份证18位  02身份证15位
	 */
	private String idType;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 性别  1男 2女
	 */
	private String sex;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 账户类型   0个人账户  1企业账户
	 */
	private String accType;
	/**
	 * 邮箱地址
	 */
	private String email;
	/**
	 * 请求方用户id
	 */
	private String userId;
	/**
	 * 营业执照编号
	 */
	private String busId;
	/**
	 * 税务登记号
	 */
	private String taxId;
	/**
	 * 渠道推荐码
	 */
	private String adNo;
	/**
	 * 账户类型  0基金账户 1靠档计息账户 2活期账户
	 */
	private String accountType;
	/**
	 * 基金公司代码
	 */
	private String fucomCode;
	/**
	 * 请求方保留信息
	 */
	private String info;
	/**
	 * 对公账户号
	 */
	private String cAccount;
	/**
	 * 营业执照编号1
	 */
	private String busIdOne;
	/**
	 * 保留域
	 */
	private String revers;
	
	
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBusId() {
		return busId;
	}
	public void setBusId(String busId) {
		this.busId = busId;
	}
	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	public String getAdNo() {
		return adNo;
	}
	public void setAdNo(String adNo) {
		this.adNo = adNo;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getFucomCode() {
		return fucomCode;
	}
	public void setFucomCode(String fucomCode) {
		this.fucomCode = fucomCode;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getcAccount() {
		return cAccount;
	}
	public void setcAccount(String cAccount) {
		this.cAccount = cAccount;
	}
	public String getBusIdOne() {
		return busIdOne;
	}
	public void setBusIdOne(String busIdOne) {
		this.busIdOne = busIdOne;
	}
	public String getRevers() {
		return revers;
	}
	public void setRevers(String revers) {
		this.revers = revers;
	}
	
	
}
