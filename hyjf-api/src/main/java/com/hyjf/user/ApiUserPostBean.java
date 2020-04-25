/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: lb
 * @version: 1.0
 * Created at: 2017年9月15日 上午9:23:07
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.user;

import com.hyjf.base.bean.BaseBean;

/**
 * @author liubin
 */

public class ApiUserPostBean extends BaseBean {
	/** 加密的汇晶社用户ID */
	private String bindUniqueIdScy;
	/**
	 * bindUniqueIdScy
	 * @return the bindUniqueIdScy
	 */
	
	public String getBindUniqueIdScy() {
		return bindUniqueIdScy;
	}
	
	public void setBindUniqueIdScy(String bindUniqueIdScy) {
		this.bindUniqueIdScy = bindUniqueIdScy;
	}
}

	