package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

import com.hyjf.mybatis.model.auto.BorrowFinserCharge;

public class FinserChargeCustomize extends BorrowFinserCharge implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String projectName;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	

}
