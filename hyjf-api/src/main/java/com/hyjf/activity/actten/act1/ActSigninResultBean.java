package com.hyjf.activity.actten.act1;

import com.hyjf.base.bean.BaseResultBean;

public class ActSigninResultBean extends BaseResultBean{

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;

    /**
     * 系统时间
     */
    int sysDate;
    /**
     * 签到日期
     */
    String signDate;
    /**
     * 签到总数
     */
    int signSum;
	/**
     * 领取次数
     */
    String receiveSum;
	/**
     * 今天是否签到
     */
    String signNow;
	/**
     *活动开始时间
     */
    int timeStart;
	/**
     * 活动结束时间
     */
    int timeEnd;
    
    
	public int getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(int timeStart) {
		this.timeStart = timeStart;
	}
	public int getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(int timeEnd) {
		this.timeEnd = timeEnd;
	}

    
    
    public String getSignNow() {
		return signNow;
	}
	public void setSignNow(String signNow) {
		this.signNow = signNow;
	}
	public int getSysDate() {
		return sysDate;
	}
	public void setSysDate(int sysDate) {
		this.sysDate = sysDate;
	}
	public String getSignDate() {
		return signDate;
	}
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	public int getSignSum() {
		return signSum;
	}
	public void setSignSum(int signSum) {
		this.signSum = signSum;
	}
	public String getReceiveSum() {
		return receiveSum;
	}
	public void setReceiveSum(String receiveSum) {
		this.receiveSum = receiveSum;
	}

}
