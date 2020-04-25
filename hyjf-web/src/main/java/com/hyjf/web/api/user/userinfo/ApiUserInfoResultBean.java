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
	
package com.hyjf.web.api.user.userinfo;

import java.io.Serializable;

/**
 * @author liubin
 */

public class ApiUserInfoResultBean implements Serializable{
	private static final long serialVersionUID = 3215431135432432222L;
	//资产总额
	private String totalAssets;

	/**
	 * totalAssets
	 * @return the totalAssets
	 */
	
	public String getTotalAssets() {
		return totalAssets;
	}

	/**
	 * @param totalAssets the totalAssets to set
	 */
	
	public void setTotalAssets(String totalAssets) {
		this.totalAssets = totalAssets;
	}
}

	