/**
 * 2016新年活动-奖品发放
 */	
package com.hyjf.mybatis.model.customize.admin;

import com.hyjf.mybatis.model.auto.NewyearSendPrize;

/**
 * 
 * @author zhangjinpeng
 *
 */

public class Newyear2016UserPrizeCustomize extends NewyearSendPrize {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2062818280556273772L;

	/********************页面表示用START**********************/
	private String username;
	
	private String truename;
	
	private String mobile;
	
	private String activity;
	
	private String sendStatusStr;
	
	private String sendTime;
	
	/********************页面表示用END**********************/
	
	
	/********************检索用字段START**********************/
	/**
     * 检索条件 用户名
     */
	private String usernameSrch;
	/**
     * 检索条件 真实姓名
     */
	private String truenameSrch;
	/**
     * 检索条件 手机号
     */
	private String mobileSrch;
	/**
     * 检索条件 活动编号
     */
	private Integer activitySrch;
	/**
     * 检索条件 优惠券金额
     */
	private String couponJineSrch;
	
	/**
     * 检索条件 发放时间开始
     */
    private String timeStartSrch;

    /**
     * 检索条件 发放时间结束
     */
    private String timeEndSrch;
    /**
     * 检索条件 发放状态
     */
    private Integer sendStatusSrch;
    
	/********************检索用字段END**********************/
	
	/** 翻页开始 */
    protected int limitStart = -1;
    /** 翻页结束 */
    protected int limitEnd = -1;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getSendStatusStr() {
		return sendStatusStr;
	}

	public void setSendStatusStr(String sendStatusStr) {
		this.sendStatusStr = sendStatusStr;
	}

	public String getUsernameSrch() {
		return usernameSrch;
	}

	public void setUsernameSrch(String usernameSrch) {
		this.usernameSrch = usernameSrch;
	}

	public String getTruenameSrch() {
		return truenameSrch;
	}

	public void setTruenameSrch(String truenameSrch) {
		this.truenameSrch = truenameSrch;
	}

	public String getMobileSrch() {
		return mobileSrch;
	}

	public void setMobileSrch(String mobileSrch) {
		this.mobileSrch = mobileSrch;
	}

	public Integer getActivitySrch() {
		return activitySrch;
	}

	public void setActivitySrch(Integer activitySrch) {
		this.activitySrch = activitySrch;
	}

	public String getCouponJineSrch() {
		return couponJineSrch;
	}

	public void setCouponJineSrch(String couponJineSrch) {
		this.couponJineSrch = couponJineSrch;
	}

	public String getTimeStartSrch() {
		return timeStartSrch;
	}

	public void setTimeStartSrch(String timeStartSrch) {
		this.timeStartSrch = timeStartSrch;
	}

	public String getTimeEndSrch() {
		return timeEndSrch;
	}

	public void setTimeEndSrch(String timeEndSrch) {
		this.timeEndSrch = timeEndSrch;
	}

	public Integer getSendStatusSrch() {
		return sendStatusSrch;
	}

	public void setSendStatusSrch(Integer sendStatusSrch) {
		this.sendStatusSrch = sendStatusSrch;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

}

	