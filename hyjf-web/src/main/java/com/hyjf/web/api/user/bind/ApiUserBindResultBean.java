/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: lb
 * @version: 1.0
 * Created at: 2017年9月14日 下午5:23:36
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.web.api.user.bind;

import java.io.Serializable;

/**
 * @author liubin
 */
public class ApiUserBindResultBean implements Serializable{
	private static final long serialVersionUID = 3215431135432432222L;
	//汇晶社用户ID
	private String bindUniqueIdScy;

	//汇盈金服用户名
	private String hyjfUserName;

	/**
	 * bindUniqueIdScy
	 * @return the bindUniqueIdScy
	 */
	
	public String getBindUniqueIdScy() {
		return bindUniqueIdScy;
	}

	/**
	 * @param bindUniqueIdScy the bindUniqueIdScy to set
	 */
	
	public void setBindUniqueIdScy(String bindUniqueIdScy) {
		this.bindUniqueIdScy = bindUniqueIdScy;
	}

	/**
	 * hyjfUserName
	 * @return the hyjfUserName
	 */
	
	public String getHyjfUserName() {
		return hyjfUserName;
	}

	/**
	 * @param hyjfUserName the hyjfUserName to set
	 */
	
	public void setHyjfUserName(String hyjfUserName) {
		this.hyjfUserName = hyjfUserName;
	}

}

	