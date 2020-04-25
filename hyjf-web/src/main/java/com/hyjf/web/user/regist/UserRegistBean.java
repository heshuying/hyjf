package com.hyjf.web.user.regist;

public class UserRegistBean {
    //用户ID
    private Integer userId;
//	//用户邮箱(保留字段)
//	private String email;
//	//用户名
//	private String userName;
	//password密码
	private String password;
//	//passwordConfirm密码
//	private String passwordConfirm;
	//用户手机号码
	private String phone;
	//短信验证码
	private String code;
//    //验证码
//	private String captcha;
	//推荐人
	private String recommend;
	//注册平台（0 pc 1 安卓 2IOS 3微信）
	private String registPlat;
//	//用户的第三方平台id
//	private String utmId;
	//用户ip
	private String ip;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRecommend() {
		return recommend;
	}
	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	public String getRegistPlat() {
		return registPlat;
	}
	public void setRegistPlat(String registPlat) {
		this.registPlat = registPlat;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}


}
