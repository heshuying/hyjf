/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.wechat.controller.regist;

import com.hyjf.wechat.base.BaseResultBean;

/**
 * 获取微信首页VO对象
 *
 * @author jijun
 * @version hyjf 1.0
 * @see 中午12:41
 * @since hyjf 1.0 2018年02月27日
 */
public class UserRegistResultVo extends BaseResultBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String request;

	private String successUrl;

	// add by liuyang 神策数据统计修改 20180820 start
	// 用户ID
	private String userId;
	// add by liuyang 神策数据统计修改 20180820 end
	
	//登录完成后的sign值
	private String sign;
	
	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
