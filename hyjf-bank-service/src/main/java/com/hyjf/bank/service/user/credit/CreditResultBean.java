package com.hyjf.bank.service.user.credit;

public class CreditResultBean {
	private String resultFlag;
	private String msg;
	private Object data;
	private String mobile;
	private Object calData;

	public String getResultFlag() {
		return resultFlag;
	}

	public void setResultFlag(String resultFlag) {
		this.resultFlag = resultFlag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Object getCalData() {
		return calData;
	}

	public void setCalData(Object calData) {
		this.calData = calData;
	}

}
