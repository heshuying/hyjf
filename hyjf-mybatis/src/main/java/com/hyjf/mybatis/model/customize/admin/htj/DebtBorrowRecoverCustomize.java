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

public class DebtBorrowRecoverCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * 借款编号 检索条件
	 */
	private String borrowNidSrch;
	/**
	 * 借款标题 检索条件
	 */
	private String borrowNameSrch;
	/**
	 * 出借人 检索条件
	 */
	private String usernameSrch;
	/**
	 * 出借订单号 检索条件
	 */
	private String orderNumSrch;
	/**
	 * 放款状态 检索条件
	 */
	private String isRecoverSrch;
	/**
	 * 出借时间 检索条件
	 */
	private String timeRecoverStartSrch;
	/**
	 * 出借时间 检索条件
	 */
	private String timeRecoverEndSrch;
	/**
	 * 放款时间 检索条件
	 */
	private String timeStartSrch;
	/**
	 * 放款时间 检索条件
	 */
	private String timeEndSrch;

	/**
	 * 借款标题
	 */
	private String borrowName;
	/**
	 * 借款编号
	 */
	private String borrowNid;
	/**
	 * 出借订单号
	 */
	private String orderNum;
	/**
	 * 出借人
	 */
	private String username;
	/**
	 * 出借金额
	 */
	private String account;
	/**
	 * 应放款金额
	 */
	private String accountYes;
	/**
	 * 出借金额
	 */
	private String accountPrice;
	/**
	 * 应收服务费
	 */
	private String loanFee;
	/**
	 * 放款订单号
	 */
	private String loanOrdid;
	/**
	 * 实收服务费
	 */
	private String servicePrice;
	/**
	 * 应放款
	 */
	private String recoverPrice;
	/**
	 * 已放款
	 */
	private String recoverPriceOver;
	/**
	 * 放款状态
	 */
	private String isRecover;
	/**
	 * 出借时间
	 */
	private String timeRecover;
	/**
	 * 放款时间
	 */
	private String addtime;

	/**
	 * 借款人用户ID
	 */
	private String userId;

	/**
	 * 项目类型
	 */
	private String borrowProjectTypeName;

	/**
	 * 借款期限
	 */
	private String borrowPeriod;

	/**
	 * 年化收益
	 */
	private String borrowApr;

	/**
	 * 还款方式
	 */
	private String borrowStyleName;

	/**
	 * 出借人用户名
	 */
	private String tenderUsername;

	/**
	 * 出借人用户ID
	 */
	private String tenderUserId;

	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	/**
	 * accountYes
	 * 
	 * @return the accountYes
	 */

	public String getAccountYes() {
		return accountYes;
	}

	/**
	 * @param accountYes
	 *            the accountYes to set
	 */

	public void setAccountYes(String accountYes) {
		this.accountYes = accountYes;
	}

	/**
	 * accountPrice
	 * 
	 * @return the accountPrice
	 */

	public String getAccountPrice() {
		return accountPrice;
	}

	/**
	 * @param accountPrice
	 *            the accountPrice to set
	 */

	public void setAccountPrice(String accountPrice) {
		this.accountPrice = accountPrice;
	}

	/**
	 * loanFee
	 * 
	 * @return the loanFee
	 */

	public String getLoanFee() {
		return loanFee;
	}

	/**
	 * @param loanFee
	 *            the loanFee to set
	 */

	public void setLoanFee(String loanFee) {
		this.loanFee = loanFee;
	}

	/**
	 * userId
	 * 
	 * @return the userId
	 */

	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * borrowProjectTypeName
	 * 
	 * @return the borrowProjectTypeName
	 */

	public String getBorrowProjectTypeName() {
		return borrowProjectTypeName;
	}

	/**
	 * @param borrowProjectTypeName
	 *            the borrowProjectTypeName to set
	 */

	public void setBorrowProjectTypeName(String borrowProjectTypeName) {
		this.borrowProjectTypeName = borrowProjectTypeName;
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

	/**
	 * tenderUsername
	 * 
	 * @return the tenderUsername
	 */

	public String getTenderUsername() {
		return tenderUsername;
	}

	/**
	 * @param tenderUsername
	 *            the tenderUsername to set
	 */

	public void setTenderUsername(String tenderUsername) {
		this.tenderUsername = tenderUsername;
	}

	/**
	 * tenderUserId
	 * 
	 * @return the tenderUserId
	 */

	public String getTenderUserId() {
		return tenderUserId;
	}

	/**
	 * @param tenderUserId
	 *            the tenderUserId to set
	 */

	public void setTenderUserId(String tenderUserId) {
		this.tenderUserId = tenderUserId;
	}

	/**
	 * borrowNidSrch
	 * 
	 * @return the borrowNidSrch
	 */

	public String getBorrowNidSrch() {
		return borrowNidSrch;
	}

	/**
	 * @param borrowNidSrch
	 *            the borrowNidSrch to set
	 */

	public void setBorrowNidSrch(String borrowNidSrch) {
		this.borrowNidSrch = borrowNidSrch;
	}

	/**
	 * borrowNameSrch
	 * 
	 * @return the borrowNameSrch
	 */

	public String getBorrowNameSrch() {
		return borrowNameSrch;
	}

	/**
	 * @param borrowNameSrch
	 *            the borrowNameSrch to set
	 */

	public void setBorrowNameSrch(String borrowNameSrch) {
		this.borrowNameSrch = borrowNameSrch;
	}

	/**
	 * usernameSrch
	 * 
	 * @return the usernameSrch
	 */

	public String getUsernameSrch() {
		return usernameSrch;
	}

	/**
	 * @param usernameSrch
	 *            the usernameSrch to set
	 */

	public void setUsernameSrch(String usernameSrch) {
		this.usernameSrch = usernameSrch;
	}

	/**
	 * orderNumSrch
	 * 
	 * @return the orderNumSrch
	 */

	public String getOrderNumSrch() {
		return orderNumSrch;
	}

	/**
	 * @param orderNumSrch
	 *            the orderNumSrch to set
	 */

	public void setOrderNumSrch(String orderNumSrch) {
		this.orderNumSrch = orderNumSrch;
	}

	/**
	 * isRecoverSrch
	 * 
	 * @return the isRecoverSrch
	 */

	public String getIsRecoverSrch() {
		return isRecoverSrch;
	}

	/**
	 * @param isRecoverSrch
	 *            the isRecoverSrch to set
	 */

	public void setIsRecoverSrch(String isRecoverSrch) {
		this.isRecoverSrch = isRecoverSrch;
	}

	/**
	 * timeRecoverStartSrch
	 * 
	 * @return the timeRecoverStartSrch
	 */

	public String getTimeRecoverStartSrch() {
		return timeRecoverStartSrch;
	}

	/**
	 * @param timeRecoverStartSrch
	 *            the timeRecoverStartSrch to set
	 */

	public void setTimeRecoverStartSrch(String timeRecoverStartSrch) {
		this.timeRecoverStartSrch = timeRecoverStartSrch;
	}

	/**
	 * timeRecoverEndSrch
	 * 
	 * @return the timeRecoverEndSrch
	 */

	public String getTimeRecoverEndSrch() {
		return timeRecoverEndSrch;
	}

	/**
	 * @param timeRecoverEndSrch
	 *            the timeRecoverEndSrch to set
	 */

	public void setTimeRecoverEndSrch(String timeRecoverEndSrch) {
		this.timeRecoverEndSrch = timeRecoverEndSrch;
	}

	/**
	 * timeStartSrch
	 * 
	 * @return the timeStartSrch
	 */

	public String getTimeStartSrch() {
		return timeStartSrch;
	}

	/**
	 * @param timeStartSrch
	 *            the timeStartSrch to set
	 */

	public void setTimeStartSrch(String timeStartSrch) {
		this.timeStartSrch = timeStartSrch;
	}

	/**
	 * timeEndSrch
	 * 
	 * @return the timeEndSrch
	 */

	public String getTimeEndSrch() {
		return timeEndSrch;
	}

	/**
	 * @param timeEndSrch
	 *            the timeEndSrch to set
	 */

	public void setTimeEndSrch(String timeEndSrch) {
		this.timeEndSrch = timeEndSrch;
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
	 * orderNum
	 * 
	 * @return the orderNum
	 */

	public String getOrderNum() {
		return orderNum;
	}

	/**
	 * @param orderNum
	 *            the orderNum to set
	 */

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
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
	 * servicePrice
	 * 
	 * @return the servicePrice
	 */

	public String getServicePrice() {
		return servicePrice;
	}

	/**
	 * @param servicePrice
	 *            the servicePrice to set
	 */

	public void setServicePrice(String servicePrice) {
		this.servicePrice = servicePrice;
	}

	/**
	 * recoverPrice
	 * 
	 * @return the recoverPrice
	 */

	public String getRecoverPrice() {
		return recoverPrice;
	}

	/**
	 * @param recoverPrice
	 *            the recoverPrice to set
	 */

	public void setRecoverPrice(String recoverPrice) {
		this.recoverPrice = recoverPrice;
	}

	/**
	 * recoverPriceOver
	 * 
	 * @return the recoverPriceOver
	 */

	public String getRecoverPriceOver() {
		return recoverPriceOver;
	}

	/**
	 * @param recoverPriceOver
	 *            the recoverPriceOver to set
	 */

	public void setRecoverPriceOver(String recoverPriceOver) {
		this.recoverPriceOver = recoverPriceOver;
	}

	/**
	 * isRecover
	 * 
	 * @return the isRecover
	 */

	public String getIsRecover() {
		return isRecover;
	}

	/**
	 * @param isRecover
	 *            the isRecover to set
	 */

	public void setIsRecover(String isRecover) {
		this.isRecover = isRecover;
	}

	/**
	 * timeRecover
	 * 
	 * @return the timeRecover
	 */

	public String getTimeRecover() {
		return timeRecover;
	}

	/**
	 * @param timeRecover
	 *            the timeRecover to set
	 */

	public void setTimeRecover(String timeRecover) {
		this.timeRecover = timeRecover;
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

	/**
	 * limitStart
	 * 
	 * @return the limitStart
	 */

	public int getLimitStart() {
		return limitStart;
	}

	/**
	 * @param limitStart
	 *            the limitStart to set
	 */

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	/**
	 * limitEnd
	 * 
	 * @return the limitEnd
	 */

	public int getLimitEnd() {
		return limitEnd;
	}

	/**
	 * @param limitEnd
	 *            the limitEnd to set
	 */

	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

	public String getLoanOrdid() {
		return loanOrdid;
	}

	public void setLoanOrdid(String loanOrdid) {
		this.loanOrdid = loanOrdid;
	}
	
	
}
