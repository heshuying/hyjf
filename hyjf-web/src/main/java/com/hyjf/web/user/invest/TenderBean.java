/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月7日 下午5:04:43
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.web.user.invest;

import java.util.Map;

public class TenderBean {

	private String action ;
	private Map<String,String> allParams;
	/**
	 * action
	 * @return the action
	 */
	
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * allParams
	 * @return the allParams
	 */
	
	public Map<String, String> getAllParams() {
		return allParams;
	}
	/**
	 * @param allParams the allParams to set
	 */
	
	public void setAllParams(Map<String, String> allParams) {
		this.allParams = allParams;
	}
	
}

	