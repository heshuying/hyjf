package com.hyjf.api.server.user.register;

import com.hyjf.base.bean.BaseBean;

/**
 * 用户注册请求Bean
 *
 * @author liuyang
 *
 */
public class UserRegisterRequestBean extends BaseBean {
	// 手机号
	private String mobile;
	//推荐人
	private String recommended;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRecommended() {
		return recommended;
	}

	public void setRecommended(String recommended) {
		this.recommended = recommended;
	}
}
