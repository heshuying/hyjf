package com.hyjf.server.module.wkcd.user.withdraw;

public class BankCardBean {
	private Integer id;//ID
    private String bank;//发卡行的名称
    private String bankCode;//发卡行的代号
    private String cardNo;//银行卡号 
    private String isDefault;//0普通提现卡1默认提现卡2快捷支付卡

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}
	
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
}
