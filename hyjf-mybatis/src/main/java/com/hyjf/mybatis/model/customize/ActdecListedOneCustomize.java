/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2018
 * Company: HYJF Corporation
 * @author: PC-LIUSHOUYI
 * @version: 1.0
 * Created at: 2018年1月31日 上午11:24:34
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

import com.hyjf.mybatis.model.auto.ActdecListedOne;

/**
 * @author PC-LIUSHOUYI
 */

public class ActdecListedOneCustomize extends ActdecListedOne implements Serializable {

	/**
	 * serialVersionUID
	 */
		
	private static final long serialVersionUID = 1L;
	
	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;

	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	/**
	 * 检索条件 用户名
	 */
	private String userNameSrch;
	
	/**
	 * 检索条件 姓名
	 */
	private String userTureNameSrch;
	
	/**
	 * 检索条件 手机号
	 */
	private String userMobileSrch;
	
	/**
	 * 检索条件 标的编号
	 */
	private String numberSrch;
	
	/**
	 * 检索条件 订单号
	 */
	private String orderNumberSrch;
	
	/**
	 * 检索条件 获得奖励
	 */
	private Integer rewardSrch;
	
	/**
	 * 检索条件 是否领取
	 */
	private String whetherSrch;
	
	/**
	 * 检索条件 领取时间
	 */
	private String createTimeStartSrch;
	
	/**
	 * 检索条件 领取时间
	 */
	private String createTimeEndSrch;
	
	/**
	 * limitStart
	 * @return the limitStart
	 */
	
	public int getLimitStart() {
		return limitStart;
	}

	/**
	 * @param limitStart the limitStart to set
	 */
	
	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	/**
	 * limitEnd
	 * @return the limitEnd
	 */
	
	public int getLimitEnd() {
		return limitEnd;
	}

	/**
	 * @param limitEnd the limitEnd to set
	 */
	
	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

	/**
	 * userNameSrch
	 * @return the userNameSrch
	 */
	
	public String getUserNameSrch() {
		return userNameSrch;
	}

	/**
	 * @param userNameSrch the userNameSrch to set
	 */
	
	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}

	/**
	 * userTureNameSrch
	 * @return the userTureNameSrch
	 */
	
	public String getUserTureNameSrch() {
		return userTureNameSrch;
	}

	/**
	 * @param userTureNameSrch the userTureNameSrch to set
	 */
	
	public void setUserTureNameSrch(String userTureNameSrch) {
		this.userTureNameSrch = userTureNameSrch;
	}

	/**
	 * userMobileSrch
	 * @return the userMobileSrch
	 */
	
	public String getUserMobileSrch() {
		return userMobileSrch;
	}

	/**
	 * @param userMobileSrch the userMobileSrch to set
	 */
	
	public void setUserMobileSrch(String userMobileSrch) {
		this.userMobileSrch = userMobileSrch;
	}

	/**
	 * numberSrch
	 * @return the numberSrch
	 */
	
	public String getNumberSrch() {
		return numberSrch;
	}

	/**
	 * @param numberSrch the numberSrch to set
	 */
	
	public void setNumberSrch(String numberSrch) {
		this.numberSrch = numberSrch;
	}

	/**
	 * orderNumberSrch
	 * @return the orderNumberSrch
	 */
	
	public String getOrderNumberSrch() {
		return orderNumberSrch;
	}

	/**
	 * @param orderNumberSrch the orderNumberSrch to set
	 */
	
	public void setOrderNumberSrch(String orderNumberSrch) {
		this.orderNumberSrch = orderNumberSrch;
	}

	/**
	 * rewardSrch
	 * @return the rewardSrch
	 */
	
	public Integer getRewardSrch() {
		return rewardSrch;
	}

	/**
	 * @param rewardSrch the rewardSrch to set
	 */
	
	public void setRewardSrch(Integer rewardSrch) {
		this.rewardSrch = rewardSrch;
	}

	/**
	 * whetherSrch
	 * @return the whetherSrch
	 */
	
	public String getWhetherSrch() {
		return whetherSrch;
	}

	/**
	 * @param whetherSrch the whetherSrch to set
	 */
	
	public void setWhetherSrch(String whetherSrch) {
		this.whetherSrch = whetherSrch;
	}

	/**
	 * createTimeStartSrch
	 * @return the createTimeStartSrch
	 */
	
	public String getCreateTimeStartSrch() {
		return createTimeStartSrch;
	}

	/**
	 * @param createTimeStartSrch the createTimeStartSrch to set
	 */
	
	public void setCreateTimeStartSrch(String createTimeStartSrch) {
		this.createTimeStartSrch = createTimeStartSrch;
	}

	/**
	 * createTimeEndSrch
	 * @return the createTimeEndSrch
	 */
	
	public String getCreateTimeEndSrch() {
		return createTimeEndSrch;
	}

	/**
	 * @param createTimeEndSrch the createTimeEndSrch to set
	 */
	
	public void setCreateTimeEndSrch(String createTimeEndSrch) {
		this.createTimeEndSrch = createTimeEndSrch;
	}
}

	