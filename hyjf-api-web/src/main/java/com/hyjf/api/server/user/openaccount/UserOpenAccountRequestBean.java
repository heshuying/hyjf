package com.hyjf.api.server.user.openaccount;

import com.hyjf.base.bean.BaseBean;

/**
 * 用户开户请求Bean
 * 
 * @author liuyang
 *
 */
public class UserOpenAccountRequestBean extends BaseBean {

	// 手机号
	private String mobile;
	// 姓名
	private String trueName;
	// 身份证号
	private String idNo;
	// 银行卡号
	private String cardNo;
	// 手机验证码
	private String smsCode;
	// 发送短信的订单号
	private String orderId;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
