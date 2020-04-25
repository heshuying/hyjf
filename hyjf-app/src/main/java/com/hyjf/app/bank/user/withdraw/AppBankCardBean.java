package com.hyjf.app.bank.user.withdraw;

//import com.hyjf.app.BaseBean;

public class AppBankCardBean {
	private Integer id;// ID
	private String bank;// 发卡行的名称
	private String bankCode;// 发卡行的代号

	private String logo;// 发卡行logo的url

	private String cardNo;// 银行卡号

	private String isDefault ="0";// 0普通提现卡1默认提现卡2快捷支付卡

	//add by xiashuqing 20171205 app2.1改版新增 begin
	//卡片描述
	private String desc;
	// 温馨提示url
	private String notice;
	//add by xiashuqing 20171205 app2.1改版新增 end
	// 合规改造 需要银行卡统一脱敏显示
    private String cardNo_info = "";

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

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

    public String getCardNo_info() {
        return cardNo_info;
    }

    public void setCardNo_info(String cardNo_info) {
        this.cardNo_info = cardNo_info;
    }
}
