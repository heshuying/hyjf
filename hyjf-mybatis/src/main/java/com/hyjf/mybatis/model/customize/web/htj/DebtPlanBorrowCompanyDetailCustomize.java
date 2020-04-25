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

public class DebtPlanBorrowCompanyDetailCustomize implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 1896382103843595521L;
	/* 项目描述 borrowContents */
	private String borrowContents;
	/* 所在地区 borrowAddress */
	private String borrowAddress;
	/* 所属行业 borrowIndustry */
	private String borrowIndustry;
	/* 注册资本 borrowCapital */
	private String regCaptial;
	/* 注册时间 registTime */
	private String registTime;
	/* 财务状况 */
	private String accountContents;

	public DebtPlanBorrowCompanyDetailCustomize() {
		super();
	}

	public String getBorrowContents() {
		return borrowContents;
	}

	public void setBorrowContents(String borrowContents) {
		this.borrowContents = borrowContents;
	}

	public String getBorrowAddress() {
		return borrowAddress;
	}

	public void setBorrowAddress(String borrowAddress) {
		this.borrowAddress = borrowAddress;
	}

	public String getBorrowIndustry() {
		return borrowIndustry;
	}

	public void setBorrowIndustry(String borrowIndustry) {
		this.borrowIndustry = borrowIndustry;
	}

	public String getRegCaptial() {
		return regCaptial;
	}

	public void setRegCaptial(String regCaptial) {
		this.regCaptial = regCaptial;
	}

	public String getRegistTime() {
		return registTime;
	}

	public void setRegistTime(String registTime) {
		this.registTime = registTime;
	}

	public String getAccountContents() {
		return accountContents;
	}

	public void setAccountContents(String accountContents) {
		this.accountContents = accountContents;
	}

}
