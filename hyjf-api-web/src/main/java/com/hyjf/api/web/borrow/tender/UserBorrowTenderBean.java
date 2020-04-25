package com.hyjf.api.web.borrow.tender;

import java.io.Serializable;

public class UserBorrowTenderBean implements Serializable  {

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
	 * 汇盈平台用户名
	 */
	private String usernamep;
	
	/**
	 * 请求时间戳
	 */
	private String timestamp;
	
	/**
	 * md5校验码
	 */
	private String sign;
	
	/**
	 * 开始时间
	 */
	private String starttime;
	
	/**
	 * 结束时间
	 */
	private String endtime;
	

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

	public String getUsernamep() {
		return usernamep;
	}

	public void setUsernamep(String usernamep) {
		this.usernamep = usernamep;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	
}
