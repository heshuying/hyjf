/**
 * Description:按照用户名/手机号查询汇付绑卡关系用接口返回类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 刘彬
 * @version: 1.0
 *           Created at: 2017年07月07日 下午2:33:39
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.callcenter.account.huifu;

import java.io.Serializable;

public class AccountHuifuBean implements Serializable  {
	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = 2569541349432465432L;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 银行卡号
	 */
	private String account;
	/**
	 * 所属银行
	 */
	private String bank;
	/**
	 * 是否默认
	 */
	private String cardType;
	/**
	 * 汇付银行卡属性
	 */
	private String cardProperty;
	/**
	 * 添加时间
	 */
	private String addTime;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardProperty() {
		return cardProperty;
	}
	public void setCardProperty(String cardProperty) {
		this.cardProperty = cardProperty;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	
}
