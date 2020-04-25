package com.hyjf.api.server.user.recharge;

import com.hyjf.base.bean.BaseBean;

/**
 * 外部服务接口:用户充值
 * 
 * @author liuyang
 *
 */
public class UserRechargeRequestBean extends BaseBean {
	// 银行卡预留手机号
	private String mobile;

	// 用户电子账户号
	private String accountId;

	// 银行卡号
	private String cardNo;

	// 短信验证码
	private String smsCode;
	// 充值金额
	private String account;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

}
