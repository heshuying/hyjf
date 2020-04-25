/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: lb
 * @version: 1.0
 * Created at: 2017年7月25日 下午12:51:21
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.callcenter.base;

/**
 * 呼叫中心接口传入类的基类
 * @author 刘彬
 */

public class BaseFormBean {
	//调用接口的唯一识别号
	String uniqueNo;
	/**
	 * 请求时间戳
	 */
	private String timeStamp;
	/**
	 * md5校验码
	 */
	private String sign;
	
	/**
	 * 接口调用唯一识别号
	 * @return the uniqueNo
	 */
	public String getUniqueNo() {
		return uniqueNo;
	}
	/**
	 * @param uniqueNo the uniqueNo to set
	 */
	
	public void setUniqueNo(String uniqueNo) {
		this.uniqueNo = uniqueNo;
	}
	/**
	 * timeStamp
	 * @return the timeStamp
	 */
	
	public String getTimeStamp() {
		return timeStamp;
	}
	/**
	 * @param timeStamp the timeStamp to set
	 */
	
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	/**
	 * sign
	 * @return the sign
	 */
	
	public String getSign() {
		return sign;
	}
	/**
	 * @param sign the sign to set
	 */
	
	public void setSign(String sign) {
		this.sign = sign;
	}
}

	