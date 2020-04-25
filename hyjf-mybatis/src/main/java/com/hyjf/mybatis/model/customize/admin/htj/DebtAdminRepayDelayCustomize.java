/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月20日 下午5:24:10
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize.admin.htj;

import java.io.Serializable;

/**
 * 还款列表
 * 
 * @author 孙亮
 * @since 2015年12月19日 上午9:29:09
 */
public class DebtAdminRepayDelayCustomize implements Serializable {

	/**
	 * serialVersionUID:TODO 这个变量是干什么的
	 */

	private static final long serialVersionUID = 1L;
	private String borrowNid;
	private String userId;
	private String borrowUserName;
	private String borrowName;
	private String projectType;
	private String projectTypeName;
	private String borrowPeriod;
	private String borrowApr;
	private String borrowAccount;
	private String borrowAccountYes;
	private String repayLastTime;
	private String borrowStyle;
	private String borrowStyleName;

	/**
	 * borrowStyle
	 * 
	 * @return the borrowStyle
	 */

	public String getBorrowStyle() {
		return borrowStyle;
	}

	/**
	 * @param borrowStyle
	 *            the borrowStyle to set
	 */

	public void setBorrowStyle(String borrowStyle) {
		this.borrowStyle = borrowStyle;
	}

	/**
	 * borrowNid
	 * 
	 * @return the borrowNid
	 */

	public String getBorrowNid() {
		return borrowNid;
	}

	/**
	 * @param borrowNid
	 *            the borrowNid to set
	 */

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	/**
	 * borrowUserName
	 * 
	 * @return the borrowUserName
	 */

	public String getBorrowUserName() {
		return borrowUserName;
	}

	/**
	 * @param borrowUserName
	 *            the borrowUserName to set
	 */

	public void setBorrowUserName(String borrowUserName) {
		this.borrowUserName = borrowUserName;
	}

	/**
	 * borrowName
	 * 
	 * @return the borrowName
	 */

	public String getBorrowName() {
		return borrowName;
	}

	/**
	 * @param borrowName
	 *            the borrowName to set
	 */

	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}

	/**
	 * projectType
	 * 
	 * @return the projectType
	 */

	public String getProjectType() {
		return projectType;
	}

	/**
	 * @param projectType
	 *            the projectType to set
	 */

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * projectTypeName
	 * 
	 * @return the projectTypeName
	 */

	public String getProjectTypeName() {
		return projectTypeName;
	}

	/**
	 * @param projectTypeName
	 *            the projectTypeName to set
	 */

	public void setProjectTypeName(String projectTypeName) {
		this.projectTypeName = projectTypeName;
	}

	/**
	 * borrowPeriod
	 * 
	 * @return the borrowPeriod
	 */

	public String getBorrowPeriod() {
		return borrowPeriod;
	}

	/**
	 * @param borrowPeriod
	 *            the borrowPeriod to set
	 */

	public void setBorrowPeriod(String borrowPeriod) {
		this.borrowPeriod = borrowPeriod;
	}

	/**
	 * borrowApr
	 * 
	 * @return the borrowApr
	 */

	public String getBorrowApr() {
		return borrowApr;
	}

	/**
	 * @param borrowApr
	 *            the borrowApr to set
	 */

	public void setBorrowApr(String borrowApr) {
		this.borrowApr = borrowApr;
	}

	/**
	 * borrowAccount
	 * 
	 * @return the borrowAccount
	 */

	public String getBorrowAccount() {
		return borrowAccount;
	}

	/**
	 * @param borrowAccount
	 *            the borrowAccount to set
	 */

	public void setBorrowAccount(String borrowAccount) {
		this.borrowAccount = borrowAccount;
	}

	/**
	 * borrowAccountYes
	 * 
	 * @return the borrowAccountYes
	 */

	public String getBorrowAccountYes() {
		return borrowAccountYes;
	}

	/**
	 * @param borrowAccountYes
	 *            the borrowAccountYes to set
	 */

	public void setBorrowAccountYes(String borrowAccountYes) {
		this.borrowAccountYes = borrowAccountYes;
	}

	/**
	 * repayLastTime
	 * 
	 * @return the repayLastTime
	 */

	public String getRepayLastTime() {
		return repayLastTime;
	}

	/**
	 * @param repayLastTime
	 *            the repayLastTime to set
	 */

	public void setRepayLastTime(String repayLastTime) {
		this.repayLastTime = repayLastTime;
	}

	/**
	 * borrowStyleName
	 * 
	 * @return the borrowStyleName
	 */

	public String getBorrowStyleName() {
		return borrowStyleName;
	}

	/**
	 * @param borrowStyleName
	 *            the borrowStyleName to set
	 */

	public void setBorrowStyleName(String borrowStyleName) {
		this.borrowStyleName = borrowStyleName;
	}

}
