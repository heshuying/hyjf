/**
 * 2016新年活动-奖品发放
 */	
package com.hyjf.mybatis.model.customize.admin;

import com.hyjf.mybatis.model.auto.NewyearCaisheCardUser;

/**
 * 
 * @author zhangjinpeng
 *
 */

public class Newyear2016UserCardCustomize extends NewyearCaisheCardUser {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2062818280556273772L;

	/********************页面表示用START**********************/
	private String username;
	
	private String truename;
	
	private String mobile;
	
	private String operateTypeStr;
	
	private String cardSourceStr;
	
	private String cardTypeStr;
	
	private String operateTime;
	
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
     * 检索条件 操作类别
     */
	private Integer operateTypeSrch;
	/**
     * 检索条件 财神卡来源途径
     */
	private Integer cardSourceSrch;
	/**
     * 检索条件 财神卡类别
     */
	private Integer cardTypeSrch;
	/**
     * 检索条件 操作时间开始
     */
    private String timeStartSrch;
    /**
     * 检索条件 操作时间结束
     */
    private String timeEndSrch;
    /**
     * 检索条件 备注
     */
    private String remarkSrch;
    
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

	public String getOperateTypeStr() {
		return operateTypeStr;
	}

	public void setOperateTypeStr(String operateTypeStr) {
		this.operateTypeStr = operateTypeStr;
	}

	public String getCardSourceStr() {
		return cardSourceStr;
	}

	public void setCardSourceStr(String cardSourceStr) {
		this.cardSourceStr = cardSourceStr;
	}

	public String getCardTypeStr() {
		return cardTypeStr;
	}

	public void setCardTypeStr(String cardTypeStr) {
		this.cardTypeStr = cardTypeStr;
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
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

	public Integer getOperateTypeSrch() {
		return operateTypeSrch;
	}

	public void setOperateTypeSrch(Integer operateTypeSrch) {
		this.operateTypeSrch = operateTypeSrch;
	}

	public Integer getCardSourceSrch() {
		return cardSourceSrch;
	}

	public void setCardSourceSrch(Integer cardSourceSrch) {
		this.cardSourceSrch = cardSourceSrch;
	}

	public Integer getCardTypeSrch() {
		return cardTypeSrch;
	}

	public void setCardTypeSrch(Integer cardTypeSrch) {
		this.cardTypeSrch = cardTypeSrch;
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

	public String getRemarkSrch() {
		return remarkSrch;
	}

	public void setRemarkSrch(String remarkSrch) {
		this.remarkSrch = remarkSrch;
	}

}

	