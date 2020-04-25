/**
 * Description:用户开户列表前端显示查询所用po
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize.web.htj;

import java.io.Serializable;

/**
 * @author 王坤
 */

public class DebtPlanBorrowPersonDetailCustomize implements Serializable {

	private static final long serialVersionUID = -1728729824292183963L;
	/* 项目描述 borrowContents */
	private String borrowContents;
	/* 年龄 age */
	private String age;
	/* 性别 sex */
	private String sex;
	/* 婚姻状况 maritalStatus */
	private String maritalStatus;
	/* 工作城市 workingCity */
	private String workingCity;
	/* 财务状况 */
	private String accountContents;
	/* 岗位类别 */
	private String position;

	public DebtPlanBorrowPersonDetailCustomize() {
		super();
	}

	public String getBorrowContents() {
		return borrowContents;
	}

	public void setBorrowContents(String borrowContents) {
		this.borrowContents = borrowContents;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getWorkingCity() {
		return workingCity;
	}

	public void setWorkingCity(String workingCity) {
		this.workingCity = workingCity;
	}

	public String getAccountContents() {
		return accountContents;
	}

	public void setAccountContents(String accountContents) {
		this.accountContents = accountContents;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
