/**
 * Description:用户详情显示类PO
 * Copyright: Copyright (c)2015
 * Company: 
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月12日 下午4:10:37
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize.admin;

/**
 * @author 王坤
 */

public class AdminUserRecommendCustomize {

	/** 用户id */
	private String userId;
	/** 用户名 */
	public String userName;
	/** 推荐人 */
	public String recommendName;
	/** 说明 */
	public String remark;
	/** ip */
	public String ip;

	private String startTime;

	private String endTime;

	private String idCard;
	
	private String trueName;
	/**
	 * startTime
	 * 
	 * @return the startTime
	 */

	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * endTime
	 * 
	 * @return the endTime
	 */

	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * 构造方法不含参数
	 */
	public AdminUserRecommendCustomize() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRecommendName() {
		return recommendName;
	}

	public void setRecommendName(String recommendName) {
		this.recommendName = recommendName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * idCard
	 * @return the idCard
	 */
		
	public String getIdCard() {
		return idCard;
			
	}

	/**
	 * @param idCard the idCard to set
	 */
		
	public void setIdCard(String idCard) {
		this.idCard = idCard;
			
	}

	/**
	 * trueName
	 * @return the trueName
	 */
		
	public String getTrueName() {
		return trueName;
			
	}

	/**
	 * @param trueName the trueName to set
	 */
		
	public void setTrueName(String trueName) {
		this.trueName = trueName;
			
	}

}
