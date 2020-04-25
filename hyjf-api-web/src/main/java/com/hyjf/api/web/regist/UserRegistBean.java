package com.hyjf.api.web.regist;

import java.io.Serializable;

public class UserRegistBean implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2569482809922162226L;

	/**
	 * 请求来源
	 */
	private String from;
	
	/**
	 * 对方平台用户名
	 */
	private String username;
	
	/**
	 * 新生成平台用户名
	 */
	private String usernameSelf;
	
	/**
	 * 手机
	 */
	private String mobile;
	
	/**
	 * 邮箱（可选）
	 */
	private String email;
	
	/**
	 * 请求时间戳
	 */
	private String timestamp;
	
	/**
	 * 注册时间戳
	 */
	private String regtime;
	
	/**
	 * md5校验码
	 */
	private String sign;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getUsernameSelf() {
		return usernameSelf;
	}

	public void setUsernameSelf(String usernameSelf) {
		this.usernameSelf = usernameSelf;
	}

	public String getRegtime() {
		return regtime;
	}

	public void setRegtime(String regtime) {
		this.regtime = regtime;
	}
	
}
