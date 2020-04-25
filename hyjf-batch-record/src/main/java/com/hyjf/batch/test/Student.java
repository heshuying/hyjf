/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: Michael
 * @version: 1.0
 * Created at: 2017年4月6日 下午4:39:43
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.batch.test;

/**
 * @author Michael
 */

/** Pojo类_Student */  
public class Student {  
	
	
	private Integer userId;
	
	private String username;
	
	private String realname;
	
	private String phone;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}

	