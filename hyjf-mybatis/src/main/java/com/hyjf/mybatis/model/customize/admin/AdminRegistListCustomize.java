/**
 * Description:用户注册记录列表前端显示所用po
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize.admin;

/**
 * @author 王坤
 */

public class AdminRegistListCustomize {

	// 用戶id
	public String userId;
	// 用戶名
	public String userName;
	// 用戶角色
	public String mobile;
	// 推荐人名称
	public String recommendName;
	//用户属性
	private String userProperty;
	//渠道id
	public String sourceId;
	//注册渠道
	public String sourceName;
	// 注册平台
	public String registPlat;
	// 注册时间
	public String regTime;
	// 注册ip
	public String regIP;

	/**
	 * 构造方法不带参数
	 */

	public AdminRegistListCustomize() {
		super();
	}

	/**
	 * 获取用户id userId
	 * 
	 * @return the userId
	 */

	public String getUserId() {
		return userId;
	}

	/**
	 * 设置用户id
	 * 
	 * @param userId
	 *            the userId to set
	 */

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 获取用户名 userName
	 * 
	 * @return the userName
	 */

	public String getUserName() {
		return userName;
	}

	/**
	 * 设置用户名
	 * 
	 * @param userName
	 *            the userName to set
	 */

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 获取用户角色 userRole
	 * 
	 * @return the userRole
	 */

	public String getMobile() {
		return mobile;
	}

	/**
	 * 设置用户角色
	 * 
	 * @param userRole
	 *            the userRole to set
	 */

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * 获取推荐人 recommendName
	 * 
	 * @return the recommendName
	 */

	public String getRecommendName() {
		return recommendName;
	}

	/**
	 * 设置推荐人
	 * 
	 * @param recommendName
	 *            the recommendName to set
	 */

	public void setRecommendName(String recommendName) {
		this.recommendName = recommendName;
	}

	/**
	 * registPlat
	 * @return the registPlat
	 */
	
	public String getRegistPlat() {
		return registPlat;
	}

	/**
	 * @param registPlat the registPlat to set
	 */
	
	public void setRegistPlat(String registPlat) {
		this.registPlat = registPlat;
	}

	/**
	 * 获取用户注册时间 regTime
	 * 
	 * @return the regTime
	 */

	public String getRegTime() {
		return regTime;
	}

	/**
	 * 设置注册时间
	 * 
	 * @param regTime
	 *            the regTime to set
	 */

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getUserProperty() {
		return userProperty;
	}

	public void setUserProperty(String userProperty) {
		this.userProperty = userProperty;
	}

    public String getRegIP() {
        return regIP;
    }

    public void setRegIP(String regIP) {
        this.regIP = regIP;
    }


}
