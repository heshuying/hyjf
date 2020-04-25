package com.hyjf.api.aems.asset;

import java.io.Serializable;

public class AemsAssetSearchBean implements Serializable {

	private static final long serialVersionUID = -9133593440657771846L;

	// 下面是必须参数
    private String assetId;

//    private String instCode;
//
//    private Integer assetType;

    private String truename;

    private String idcard;

    private Long account;

    private Integer borrowPeriod;

    private String borrowStyle;
    
    // 返回参数
    private String retCode;
    
    private String retMsg;
    
    
    // 可选参数
    private Integer sex;

    private Integer age;

    private Integer marriage;

    private String workCity;

    private String position;

    private String domicile;

    private String creditLevel;

    private String useage;

    private String monthlyIncome;

    private String firstPayment;

    private String secondPayment;

    private String costIntrodution;

    private String assetInfo;

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

//	public String getInstCode() {
//		return instCode;
//	}
//
//	public void setInstCode(String instCode) {
//		this.instCode = instCode;
//	}
//
//	public Integer getAssetType() {
//		return assetType;
//	}
//
//	public void setAssetType(Integer assetType) {
//		this.assetType = assetType;
//	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public Long getAccount() {
		return account;
	}

	public void setAccount(Long account) {
		this.account = account;
	}

	public Integer getBorrowPeriod() {
		return borrowPeriod;
	}

	public void setBorrowPeriod(Integer borrowPeriod) {
		this.borrowPeriod = borrowPeriod;
	}

	public String getBorrowStyle() {
		return borrowStyle;
	}

	public void setBorrowStyle(String borrowStyle) {
		this.borrowStyle = borrowStyle;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getMarriage() {
		return marriage;
	}

	public void setMarriage(Integer marriage) {
		this.marriage = marriage;
	}

	public String getWorkCity() {
		return workCity;
	}

	public void setWorkCity(String workCity) {
		this.workCity = workCity;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDomicile() {
		return domicile;
	}

	public void setDomicile(String domicile) {
		this.domicile = domicile;
	}

	public String getCreditLevel() {
		return creditLevel;
	}

	public void setCreditLevel(String creditLevel) {
		this.creditLevel = creditLevel;
	}

	public String getUseage() {
		return useage;
	}

	public void setUseage(String useage) {
		this.useage = useage;
	}

	public String getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(String monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public String getFirstPayment() {
		return firstPayment;
	}

	public void setFirstPayment(String firstPayment) {
		this.firstPayment = firstPayment;
	}

	public String getSecondPayment() {
		return secondPayment;
	}

	public void setSecondPayment(String secondPayment) {
		this.secondPayment = secondPayment;
	}

	public String getCostIntrodution() {
		return costIntrodution;
	}

	public void setCostIntrodution(String costIntrodution) {
		this.costIntrodution = costIntrodution;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public String getAssetInfo() {
		return assetInfo;
	}

	public void setAssetInfo(String assetInfo) {
		this.assetInfo = assetInfo;
	}
    
	
	
}
