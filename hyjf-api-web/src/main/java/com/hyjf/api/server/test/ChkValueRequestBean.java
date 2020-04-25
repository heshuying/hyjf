package com.hyjf.api.server.test;

import com.hyjf.base.bean.BaseBean;

/**
 * 
 * 外部服务接口:用户免密提现请求Bean
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年9月6日
 * @see 下午4:22:33
 */
public class ChkValueRequestBean extends BaseBean {

    // 请求类型
    private String type;
	// 用户电子账户号  
	private String accountId;
	// 银行卡号
	private String cardNo;
	// 提现金额
	private String account;
	
	// 提现平台
	private String platform;
	// 银行类型
	private String bankType;
	// 银联行号
	private String payAllianceCode;
	// 手机号
	private  String  mobile;
	// 姓名
	private String trueName;
	// 身份证号
	private String idNo;
	// 短信验证码
	private String smsCode;
	// 短信发送订单号
	private String  orderId;
	// 短信发送订单号
    private String  status;
    // 短信发送订单号
    private String  statusDesc;

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

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTrueName() {
		return this.trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getIdNo() {
		return this.idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getSmsCode() {
		return this.smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
	
}
