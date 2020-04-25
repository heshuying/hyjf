package com.hyjf.vip.apply;

import com.hyjf.base.bean.BaseBean;

public class ApplyBean extends BaseBean {

	private String callBackUrl;
	
	private String platform;

	public String getCallBackUrl() {
		return callBackUrl;
	}

	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

}
