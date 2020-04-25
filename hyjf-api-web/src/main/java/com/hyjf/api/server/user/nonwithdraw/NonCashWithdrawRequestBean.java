package com.hyjf.api.server.user.nonwithdraw;

import com.hyjf.base.bean.BaseBean;

/**
 * 
 * 外部服务接口:用户免密提现请求Bean
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年9月6日
 * @see 下午4:22:33
 */
public class NonCashWithdrawRequestBean extends BaseBean {

	// 用户电子账户号  
	private String accountId;
	// 银行卡号
	private String cardNo;
	// 提现金额
	private String account;
	// 银行类型
	private String bankType;
	// 银联行号
	private String payAllianceCode;

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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}


	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getPayAllianceCode() {
		return payAllianceCode;
	}

	public void setPayAllianceCode(String payAllianceCode) {
		this.payAllianceCode = payAllianceCode;
	}
}
