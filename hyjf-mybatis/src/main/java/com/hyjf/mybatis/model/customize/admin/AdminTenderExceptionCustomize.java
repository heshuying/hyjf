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

package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;

/**
 * @author Administrator
 */

public class AdminTenderExceptionCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;

	private String borrowAccountYes;

	private String borrowAccount;

	private String borrowNidSrch;
	/**
	 * 检索条件 出借订单号
	 */
	private String nidSrch;

	/**
	 * 检索条件 出借人
	 */
	private String tenderUserNameSrch;

	/**
	 * 检索条件 时间开始
	 */
	private String timeStartSrch;

	/**
	 * 检索条件 时间结束
	 */
	private String timeEndSrch;

	/**
	 * 出借订单号
	 */
	private String nid;
	/**
	 * 借款标题
	 */
	private String borrowName;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 推荐人
	 */
	private String referrerName;
	/**
	 * 借款编号
	 */
	private String borrowNid;
	/**
	 * 年化利率
	 */
	private String borrowApr;
	/**
	 * 借款期限
	 */
	private String borrowPeriod;
	/**
	 * 还款方式
	 */
	private String borrowStyleName;
	/**
	 * 出借金额
	 */
	private String account;
	/**
	 * 操作平台
	 */
	private String operatingDeck;
	/**
	 * 出借时间
	 */
	private String addtime;

	/**
	 * 操作平台
	 */
	private String client;

	/**
	 * 出借方式
	 * 
	 */
	private String investType;

	/**
	 * 用户属性
	 */
	private String attribute;

	/**
	 * nidSrch
	 * 
	 * @return the nidSrch
	 */

	public String getNidSrch() {
		return nidSrch;
	}

	/**
	 * @param nidSrch
	 *            the nidSrch to set
	 */

	public void setNidSrch(String nidSrch) {
		this.nidSrch = nidSrch;
	}

	/**
	 * tenderUserNameSrch
	 * 
	 * @return the tenderUserNameSrch
	 */

	public String getTenderUserNameSrch() {
		return tenderUserNameSrch;
	}

	/**
	 * @param tenderUserNameSrch
	 *            the tenderUserNameSrch to set
	 */

	public void setTenderUserNameSrch(String tenderUserNameSrch) {
		this.tenderUserNameSrch = tenderUserNameSrch;
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
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

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
	 * nid
	 * 
	 * @return the nid
	 */

	public String getNid() {
		return nid;
	}

	/**
	 * @param nid
	 *            the nid to set
	 */

	public void setNid(String nid) {
		this.nid = nid;
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
	 * referrerName
	 * 
	 * @return the referrerName
	 */

	public String getReferrerName() {
		return referrerName;
	}

	/**
	 * @param referrerName
	 *            the referrerName to set
	 */

	public void setReferrerName(String referrerName) {
		this.referrerName = referrerName;
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
	 * operatingDeck
	 * 
	 * @return the operatingDeck
	 */

	public String getOperatingDeck() {
		return operatingDeck;
	}

	/**
	 * @param operatingDeck
	 *            the operatingDeck to set
	 */

	public void setOperatingDeck(String operatingDeck) {
		this.operatingDeck = operatingDeck;
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

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getInvestType() {
		return investType;
	}

	public void setInvestType(String investType) {
		this.investType = investType;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

}
