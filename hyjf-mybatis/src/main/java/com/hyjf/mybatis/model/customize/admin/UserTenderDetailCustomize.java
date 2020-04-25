/**
 * 10月份活动，用户使用推荐星实体类
 */	
package com.hyjf.mybatis.model.customize.admin;

import java.math.BigDecimal;

/**
 * 
 * @author zhangjinpeng
 *
 */

public class UserTenderDetailCustomize {
	
	/********************页面表示用START**********************/
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * PC端渠道名称
	 */
	private String pcUtmSource;
	
	/**
	 * APP渠道名称
	 */
	private String appUtmSource;
	
	/**
	 * 注册时间
	 */
	private String regTime;
	
	/**
	 * 开户时间
	 */
	private String openAccountTime;
	
	/**
	 * 出借金额
	 */
	private BigDecimal tenderAccount;
	
	/**
	 * 借款编号
	 */
	private String borrowNid;
	
	/**
	 * 标的期限
	 */
	private String borrowPeriod;
	
	/**
	 * 出借时间
	 */
	private String tenderTime;
	
	/********************页面表示用END**********************/
	
	
	/********************检索用字段START**********************/
	
	/**
	 * 用户名（检索用）
	 */
	private String userNameSrch;
	
	/**
	 * 渠道名（检索用）
	 */
	private String channelNameSrch;
	
	/**
	 * 注册时间开始（检索用）
	 */
	private String regTimeStartSrch;
	
	/**
	 * 注册时间截止（检索用）
	 */
	private String regTimeEndSrch;
	
	/**
	 * 开户时间开始（检索用）
	 */
	private String openAccountTimeStartSrch;
	
	/**
	 * 开户时间截止（检索用）
	 */
	private String openAccountTimeEndSrch;
	
	/**
	 * 借款编号（检索用）
	 */
	private String borrowNidSrch;
	
	/**
	 * 出借时间开始（检索用）
	 */
	private String tenderTimeStartSrch;
	
	/**
	 * 出借时间截止（检索用）
	 */
	private String tenderTimeEndSrch;
	
	
	/********************检索用字段END**********************/
	
	/** 翻页开始 */
    protected int limitStart = -1;
    /** 翻页结束 */
    protected int limitEnd = -1;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRegTime() {
		return regTime;
	}
	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}
	public String getOpenAccountTime() {
		return openAccountTime;
	}
	public void setOpenAccountTime(String openAccountTime) {
		this.openAccountTime = openAccountTime;
	}
	public BigDecimal getTenderAccount() {
		return tenderAccount;
	}
	public void setTenderAccount(BigDecimal tenderAccount) {
		this.tenderAccount = tenderAccount;
	}
	public String getBorrowNid() {
		return borrowNid;
	}
	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}
	public String getTenderTime() {
		return tenderTime;
	}
	public void setTenderTime(String tenderTime) {
		this.tenderTime = tenderTime;
	}
	public String getUserNameSrch() {
		return userNameSrch;
	}
	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}
	public String getChannelNameSrch() {
		return channelNameSrch;
	}
	public void setChannelNameSrch(String channelNameSrch) {
		this.channelNameSrch = channelNameSrch;
	}
	public String getBorrowNidSrch() {
		return borrowNidSrch;
	}
	public void setBorrowNidSrch(String borrowNidSrch) {
		this.borrowNidSrch = borrowNidSrch;
	}
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
	public String getPcUtmSource() {
		return pcUtmSource;
	}
	public void setPcUtmSource(String pcUtmSource) {
		this.pcUtmSource = pcUtmSource;
	}
	public String getAppUtmSource() {
		return appUtmSource;
	}
	public void setAppUtmSource(String appUtmSource) {
		this.appUtmSource = appUtmSource;
	}
	public String getRegTimeStartSrch() {
		return regTimeStartSrch;
	}
	public void setRegTimeStartSrch(String regTimeStartSrch) {
		this.regTimeStartSrch = regTimeStartSrch;
	}
	public String getRegTimeEndSrch() {
		return regTimeEndSrch;
	}
	public void setRegTimeEndSrch(String regTimeEndSrch) {
		this.regTimeEndSrch = regTimeEndSrch;
	}
	public String getOpenAccountTimeStartSrch() {
		return openAccountTimeStartSrch;
	}
	public void setOpenAccountTimeStartSrch(String openAccountTimeStartSrch) {
		this.openAccountTimeStartSrch = openAccountTimeStartSrch;
	}
	public String getOpenAccountTimeEndSrch() {
		return openAccountTimeEndSrch;
	}
	public void setOpenAccountTimeEndSrch(String openAccountTimeEndSrch) {
		this.openAccountTimeEndSrch = openAccountTimeEndSrch;
	}
	public String getTenderTimeStartSrch() {
		return tenderTimeStartSrch;
	}
	public void setTenderTimeStartSrch(String tenderTimeStartSrch) {
		this.tenderTimeStartSrch = tenderTimeStartSrch;
	}
	public String getTenderTimeEndSrch() {
		return tenderTimeEndSrch;
	}
	public void setTenderTimeEndSrch(String tenderTimeEndSrch) {
		this.tenderTimeEndSrch = tenderTimeEndSrch;
	}
    public String getBorrowPeriod() {
        return borrowPeriod;
    }
    public void setBorrowPeriod(String borrowPeriod) {
        this.borrowPeriod = borrowPeriod;
    }
}

	