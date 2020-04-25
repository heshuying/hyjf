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

import com.hyjf.mybatis.model.auto.ActdecListedThree;

/**
 * @author PC-LIUSHOUYI
 */

public class ActdecListedThreeCustomize extends ActdecListedThree implements Serializable {

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
}

	