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

public class AdminRecoverExceptionCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;

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
	 * 检索条件 借款编号
	 */
	private String borrowNidSrch;

	/**
	 * 检索条件 借款标题
	 */
	private String borrowNameSrch;

	/**
	 * 检索条件 用户名
	 */
	private String usernameSrch;

	/**
	 * 检索条件 状态
	 */
	private String statusSrch;

	/**
	 * 检索条件 还款方式
	 */
	private String borrowStyleSrch;

	/**
	 * 检索条件 项目类型
	 */
	private String projectTypeSrch;

	/**
	 * 检索条件 是否交保证金
	 */
	private String isBailSrch;

	/**
	 * 放款时间 检索条件
	 */
	private String timeStartSrch;

	/**
	 * 放款时间 检索条件
	 */
	private String timeEndSrch;

	/**
	 * 借款编号
	 */
	private String borrowNid;

	/**
	 * 借款标题
	 */
	private String borrowName;

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
	 * 出借人用户名
	 */
	private String tenderUsername;

	/**
	 * 出借人用户ID
	 */
	private String tenderUserId;

	/**
	 * 年利率
	 */
	private String borrowApr;

	/**
	 * 借款期限
	 */
	private String borrowPeriod;

	/**
	 * 借到金额
	 */
	private String borrowAccountYes;

	/**
	 * 有效时间
	 */
	private String borrowValidTime;

	/**
	 * 状态
	 */
	private String status;

	/**
	 * 状态名称
	 */
	private String statusName;

	/**
	 * 融资服务费
	 */
	private String serviceScale;

	/**
	 * 账户管理费率
	 */
	private String managerScale;

	/**
	 * 发标时间
	 */
	private String ontime;

	/**
	 * 满表时间
	 */
	private String overTime;

	/**
	 * 还款方式
	 */
	private String borrowStyle;

	/**
	 * 还款方式名称
	 */
	private String borrowStyleName;

	/**
	 * 项目类型
	 */
	private String projectType;

    /**
     * 错误内容
     */
    private String data;

	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;

	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

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
	 * statusSrch
	 *
	 * @return the statusSrch
	 */

	public String getStatusSrch() {
		return statusSrch;
	}

	/**
	 * @param statusSrch
	 *            the statusSrch to set
	 */

	public void setStatusSrch(String statusSrch) {
		this.statusSrch = statusSrch;
	}

	/**
	 * borrowStyleSrch
	 *
	 * @return the borrowStyleSrch
	 */

	public String getBorrowStyleSrch() {
		return borrowStyleSrch;
	}

	/**
	 * @param borrowStyleSrch
	 *            the borrowStyleSrch to set
	 */

	public void setBorrowStyleSrch(String borrowStyleSrch) {
		this.borrowStyleSrch = borrowStyleSrch;
	}

	/**
	 * projectTypeSrch
	 *
	 * @return the projectTypeSrch
	 */

	public String getProjectTypeSrch() {
		return projectTypeSrch;
	}

	/**
	 * @param projectTypeSrch
	 *            the projectTypeSrch to set
	 */

	public void setProjectTypeSrch(String projectTypeSrch) {
		this.projectTypeSrch = projectTypeSrch;
	}

	/**
	 * isBailSrch
	 *
	 * @return the isBailSrch
	 */

	public String getIsBailSrch() {
		return isBailSrch;
	}

	/**
	 * @param isBailSrch
	 *            the isBailSrch to set
	 */

	public void setIsBailSrch(String isBailSrch) {
		this.isBailSrch = isBailSrch;
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
	 * borrowAccountYes
	 *
	 * @return the borrowAccountYes
	 */

	public String getBorrowAccountYes() {
		return borrowAccountYes;
	}

	/**
	 * statusName
	 *
	 * @return the statusName
	 */

	public String getStatusName() {
		return statusName;
	}

	/**
	 * @param statusName
	 *            the statusName to set
	 */

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	/**
	 * borrowValidTime
	 *
	 * @return the borrowValidTime
	 */

	public String getBorrowValidTime() {
		return borrowValidTime;
	}

	/**
	 * @param borrowValidTime
	 *            the borrowValidTime to set
	 */

	public void setBorrowValidTime(String borrowValidTime) {
		this.borrowValidTime = borrowValidTime;
	}

	/**
	 * managerScale
	 *
	 * @return the managerScale
	 */

	public String getManagerScale() {
		return managerScale;
	}

	/**
	 * @param managerScale
	 *            the managerScale to set
	 */

	public void setManagerScale(String managerScale) {
		this.managerScale = managerScale;
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
	 * @param borrowAccountYes
	 *            the borrowAccountYes to set
	 */

	public void setBorrowAccountYes(String borrowAccountYes) {
		this.borrowAccountYes = borrowAccountYes;
	}

	/**
	 * serviceScale
	 *
	 * @return the serviceScale
	 */

	public String getServiceScale() {
		return serviceScale;
	}

	/**
	 * @param serviceScale
	 *            the serviceScale to set
	 */

	public void setServiceScale(String serviceScale) {
		this.serviceScale = serviceScale;
	}

	/**
	 * overTime
	 *
	 * @return the overTime
	 */

	public String getOverTime() {
		return overTime;
	}

	/**
	 * @param overTime
	 *            the overTime to set
	 */

	public void setOverTime(String overTime) {
		this.overTime = overTime;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
