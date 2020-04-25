package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
/**
 * 
 * @author ZXS
 */
public class AppointmentAuthLogCustomize  implements Serializable {


	private int limitStart=0;

	private int limitEnd=999999999;
	/**
     *  主键
     */
	private int id;
	/**
     * 用户id
     */
	private int userId;	
	/**
	 * 用户名
	 */
	private String username;
	/**
	 *姓名
	 */
	private String truename;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 汇付客户号
	 */
	private String userCustId;
	/**
	 * 授权方式
	 */
	private String authType;
	/**
	 * 授权状态
	 */
	private String authStatus;
	/**
	 * 操作时间
	 */
	private String addTime;
	public int getLimitStart() {
		return limitStart;
	}
	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}
	public int getLimitEnd() {
		return limitEnd;
	}
	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserCustId() {
		return userCustId;
	}
	public void setUserCustId(String userCustId) {
		this.userCustId = userCustId;
	}
	public String getAuthType() {
		return authType;
	}
	public void setAuthType(String authType) {
		this.authType = authType;
	}
	public String getAuthStatus() {
		return authStatus;
	}
	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getTruename() {
		return truename;
	}
	public void setTruename(String truename) {
		this.truename = truename;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}




	