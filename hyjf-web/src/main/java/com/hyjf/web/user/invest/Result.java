/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月10日 下午2:01:30
 * Modification History:
 * Modified by : 
 */

package com.hyjf.web.user.invest;

/**
 * api返回结果码
 */
public class Result {

	private String error; // 错误1;成功0
	private String data; // 错误提示信息;成功返回成功信息
	private String url;

	/**
	 * url
	 * @return the url
	 */
	
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * error
	 * 
	 * @return the error
	 */

	public String getError() {
		return error;
	}

	/**
	 * @param error
	 *            the error to set
	 */

	public void setError(String error) {
		this.error = error;
	}

	/**
	 * data
	 * 
	 * @return the data
	 */

	public String getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */

	public void setData(String data) {
		this.data = data;
	}

}
