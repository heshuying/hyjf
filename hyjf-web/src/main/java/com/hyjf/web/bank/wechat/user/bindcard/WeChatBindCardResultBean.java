package com.hyjf.web.bank.wechat.user.bindcard;

import com.hyjf.mybatis.model.auto.BankCard;

public class WeChatBindCardResultBean extends BankCard {

	private static final long serialVersionUID = 1L;

	private String isDefault;// 0普通提现卡1默认提现卡2快捷支付卡

	private String logo;// 发卡行logo的url

	private String cardNoInfo;// 合规改造 需要银行卡统一脱敏显示

	private String statusCode;// 状态码

	private String statusDesc;// 状态描述

	private String idCard;// 身份证号

	private String userNameEncry;// 用户名加密

	private String idCardEncry;// 身份证号加密

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getCardNoInfo() {
		return cardNoInfo;
	}

	public void setCardNoInfo(String cardNoInfo) {
		this.cardNoInfo = cardNoInfo;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getUserNameEncry() {
		return userNameEncry;
	}

	public void setUserNameEncry(String userNameEncry) {
		this.userNameEncry = userNameEncry;
	}

	public String getIdCardEncry() {
		return idCardEncry;
	}

	public void setIdCardEncry(String idCardEncry) {
		this.idCardEncry = idCardEncry;
	}

}
