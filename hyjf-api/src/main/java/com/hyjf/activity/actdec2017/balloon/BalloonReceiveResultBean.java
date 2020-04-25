package com.hyjf.activity.actdec2017.balloon;

import com.hyjf.base.bean.BaseResultBean;

public class BalloonReceiveResultBean extends BaseResultBean{
	
	private static final long serialVersionUID = 1L;

	private String userId;
	
    private String actStartTime;
    
    private String actEndtime;
    
    private String nowTime;
    
    /**
     * 可以领取数量
     */
    private Integer canReceiveCount;
    /**
     * 已领取数量
     */
    private Integer receivedCount;
    
    /**
     * 本次领取数量
     */
    private Integer currentReceiveCount;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getActStartTime() {
		return actStartTime;
	}

	public void setActStartTime(String actStartTime) {
		this.actStartTime = actStartTime;
	}

	public String getActEndtime() {
		return actEndtime;
	}

	public void setActEndtime(String actEndtime) {
		this.actEndtime = actEndtime;
	}

	public String getNowTime() {
		return nowTime;
	}

	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}

	public Integer getCanReceiveCount() {
		return canReceiveCount;
	}

	public void setCanReceiveCount(Integer canReceiveCount) {
		this.canReceiveCount = canReceiveCount;
	}

	public Integer getReceivedCount() {
		return receivedCount;
	}

	public void setReceivedCount(Integer receivedCount) {
		this.receivedCount = receivedCount;
	}

	public Integer getCurrentReceiveCount() {
		return currentReceiveCount;
	}

	public void setCurrentReceiveCount(Integer currentReceiveCount) {
		this.currentReceiveCount = currentReceiveCount;
	}

    
}
