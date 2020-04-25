package com.hyjf.wechat.controller.user.autoplus;

import com.hyjf.wechat.base.BaseResultBean;

/**
 * 
 * 自动出借债转授权
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月2日
 * @see 上午11:41:47
 */
public class AutoPlusResultBean extends BaseResultBean {

    private static final long serialVersionUID = 5610548601588762038L;

    // 前导业务码
	private String srvAuthCode;

	// 授权状态 0未授权 1已授权
	private int userAutoStatus;

	// 跳转的url
	private String authUrl;

	public String getAuthUrl() {
		return authUrl;
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

	public String getSrvAuthCode() {
		return srvAuthCode;
	}

	public void setSrvAuthCode(String srvAuthCode) {
		this.srvAuthCode = srvAuthCode;
	}

	public int getUserAutoStatus() {
		return userAutoStatus;
	}

	public void setUserAutoStatus(int userAutoStatus) {
		this.userAutoStatus = userAutoStatus;
	}
}
