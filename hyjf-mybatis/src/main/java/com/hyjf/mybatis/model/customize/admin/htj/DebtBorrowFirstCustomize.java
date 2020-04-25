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
 * @author Administrator
 */

public class DebtBorrowFirstCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;
	/**
	 * 借款编码
	 */
	private String borrowNid;
	/**
	 * 借款标题
	 */
	private String borrowName;
	/**
	 * 借款用户
	 */
	private String username;
	/**
	 * 借款金额
	 */
	private String account;
	/**
	 * 年利率
	 */
	private String borrowApr;
	/**
	 * 借款期限
	 */
	private String borrowPeriod;
	/**
	 * 应还本息
	 */
	private String repayAccount;
	/**
	 * 借到时间
	 */
	private String createTime;
	/**
	 * 应还时间
	 */
	private String repayTime;
	/**
	 * 定时发标时间
	 */
	private String ontime;
	/**
	 * 预约开始时间
	 */
	private String bookingBeginTime;
	/**
	 * 预约截止时间
	 */
	private String bookingEndTime;
	
	/**
	 * 状态 未交保证金 OR 已交保证金
	 */
	private String status;
	/**
	 * 未交保证金 OR 已交保证金
	 */
	private String isBail;
	/**
	 * 添加时间
	 */
	private String addtime;
	/**
	 * 初审状态
	 */
	private String verifyStatus;
	/**
	 * 项目类型
	 */
	private String projectType;

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

	/**
	 * verifyStatus
	 * 
	 * @return the verifyStatus
	 */

	public String getVerifyStatus() {
		return verifyStatus;
	}

	/**
	 * @param verifyStatus
	 *            the verifyStatus to set
	 */

	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	/**
	 * serialversionuid
	 * 
	 * @return the serialversionuid
	 */

	public static long getSerialversionuid() {
		return serialVersionUID;
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
	 * username
	 * 
	 * @return the username
	 */

	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */

	public void setUsername(String username) {
		this.username = username;
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
	 * account
	 * 
	 * @return the account
	 */

	public String getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */

	public void setAccount(String account) {
		this.account = account;
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
	 * repayAccount
	 * 
	 * @return the repayAccount
	 */

	public String getRepayAccount() {
		return repayAccount;
	}

	/**
	 * @param repayAccount
	 *            the repayAccount to set
	 */

	public void setRepayAccount(String repayAccount) {
		this.repayAccount = repayAccount;
	}

	/**
	 * createTime
	 * 
	 * @return the createTime
	 */

	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * repayTime
	 * 
	 * @return the repayTime
	 */

	public String getRepayTime() {
		return repayTime;
	}

	/**
	 * @param repayTime
	 *            the repayTime to set
	 */

	public void setRepayTime(String repayTime) {
		this.repayTime = repayTime;
	}

	/**
	 * ontime
	 * 
	 * @return the ontime
	 */

	public String getOntime() {
		return ontime;
	}

	/**
	 * @param ontime
	 *            the ontime to set
	 */

	public void setOntime(String ontime) {
		this.ontime = ontime;
	}

	/**
	 * status
	 * 
	 * @return the status
	 */

	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * isBail
	 * 
	 * @return the isBail
	 */

	public String getIsBail() {
		return isBail;
	}

	/**
	 * @param isBail
	 *            the isBail to set
	 */

	public void setIsBail(String isBail) {
		this.isBail = isBail;
	}

	/**
	 * addtime
	 * 
	 * @return the addtime
	 */

	public String getAddtime() {
		return addtime;
	}

	/**
	 * @param addtime
	 *            the addtime to set
	 */

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getBookingBeginTime() {
		return bookingBeginTime;
	}

	public void setBookingBeginTime(String bookingBeginTime) {
		this.bookingBeginTime = bookingBeginTime;
	}

	public String getBookingEndTime() {
		return bookingEndTime;
	}

	public void setBookingEndTime(String bookingEndTime) {
		this.bookingEndTime = bookingEndTime;
	}
}
