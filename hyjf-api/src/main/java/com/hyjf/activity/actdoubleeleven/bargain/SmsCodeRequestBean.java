package com.hyjf.activity.actdoubleeleven.bargain;

import com.hyjf.base.bean.BaseBean;

public class SmsCodeRequestBean extends BaseBean{

	private String smsCodeType;
	private String phoneNum;
	
	public String getSmsCodeType() {
		return smsCodeType;
	}
	public void setSmsCodeType(String smsCodeType) {
		this.smsCodeType = smsCodeType;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	@Override
	public String toString() {
		return super.toString() + "SmsCodeRequestBean [smsCodeType=" + smsCodeType + ", phoneNum=" + phoneNum + "]";
	}
	
}
