package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

public class TenderSexCount implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int sex;
	public int count;
	
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
