package com.hyjf.mybatis.model.customize.web.hjh;

import java.io.Serializable;

/**
 * 
 * @author HBZ
 */
public class HjhReInvestDetailCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1421679819371458772L;

	private int limitStart = -1;

	private int limitEnd = -1;

	public int getLimitStart() {
		return limitStart;
	}

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	public int getLimitEnd() {
		return limitEnd;
	}

	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}
	
	//前画面传入
	private String planNid;
	private String date;
	//计划出借订单号（检索）
	private String accedeOrderIdSrch;
	//用户名（检索）
	private String userNameSrch;
	//借款编号（检索）
	private String borrowNidSrch;
	//借款期限(锁定期)（检索）
	private String lockPeriodSrch;
	//出借方式（检索）
	private String investTypeSrch;
	//还款方式（检索）
	private String borrowStyleSrch;

	//计划订单号
	private String accedeOrderId;
	//用户名
	private String userName;
	//推荐人
	private String inviteUserName;
	//用户属性
	private String userAttribute;
	//借款编号
	private String borrowNid;
	//年化利率
	private String expectApr;
	//借款期限(锁定期)
	private String borrowPeriod;
	private String isMonth;
	//出借金额
	private String accedeAccount;
	//还款方式
	private String borrowStyle;
	//出借方式
	private String investType;
	//计息时间
	private String countInterestTime;
	//出借时间
	private String addTime;
	
	/**
	 * accedeOrderIdSrch
	 * @return the accedeOrderIdSrch
	 */
	
	public String getAccedeOrderIdSrch() {
		return accedeOrderIdSrch;
	}

	/**
	 * @param accedeOrderIdSrch the accedeOrderIdSrch to set
	 */
	
	public void setAccedeOrderIdSrch(String accedeOrderIdSrch) {
		this.accedeOrderIdSrch = accedeOrderIdSrch;
	}

	/**
	 * userNameSrch
	 * @return the userNameSrch
	 */
	
	public String getUserNameSrch() {
		return userNameSrch;
	}

	/**
	 * @param userNameSrch the userNameSrch to set
	 */
	
	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}

	/**
	 * borrowNidSrch
	 * @return the borrowNidSrch
	 */
	
	public String getBorrowNidSrch() {
		return borrowNidSrch;
	}

	/**
	 * @param borrowNidSrch the borrowNidSrch to set
	 */
	
	public void setBorrowNidSrch(String borrowNidSrch) {
		this.borrowNidSrch = borrowNidSrch;
	}

	/**
	 * lockPeriodSrch
	 * @return the lockPeriodSrch
	 */
	
	public String getLockPeriodSrch() {
		return lockPeriodSrch;
	}

	/**
	 * @param lockPeriodSrch the lockPeriodSrch to set
	 */
	
	public void setLockPeriodSrch(String lockPeriodSrch) {
		this.lockPeriodSrch = lockPeriodSrch;
	}

	/**
	 * investTypeSrch
	 * @return the investTypeSrch
	 */
	
	public String getInvestTypeSrch() {
		return investTypeSrch;
	}

	/**
	 * @param investTypeSrch the investTypeSrch to set
	 */
	
	public void setInvestTypeSrch(String investTypeSrch) {
		this.investTypeSrch = investTypeSrch;
	}

	/**
	 * borrowStyleSrch
	 * @return the borrowStyleSrch
	 */
	
	public String getBorrowStyleSrch() {
		return borrowStyleSrch;
	}

	/**
	 * @param borrowStyleSrch the borrowStyleSrch to set
	 */
	
	public void setBorrowStyleSrch(String borrowStyleSrch) {
		this.borrowStyleSrch = borrowStyleSrch;
	}

	/**
	 * accedeOrderId
	 * @return the accedeOrderId
	 */
	
	public String getAccedeOrderId() {
		return accedeOrderId;
	}

	/**
	 * @param accedeOrderId the accedeOrderId to set
	 */
	
	public void setAccedeOrderId(String accedeOrderId) {
		this.accedeOrderId = accedeOrderId;
	}

	/**
	 * planNid
	 * @return the planNid
	 */
	
	public String getPlanNid() {
		return planNid;
	}

	/**
	 * @param planNid the planNid to set
	 */
	
	public void setPlanNid(String planNid) {
		this.planNid = planNid;
	}

	/**
	 * userName
	 * @return the userName
	 */
	
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * inviteUserName
	 * @return the inviteUserName
	 */
	
	public String getInviteUserName() {
		return inviteUserName;
	}

	/**
	 * @param inviteUserName the inviteUserName to set
	 */
	
	public void setInviteUserName(String inviteUserName) {
		this.inviteUserName = inviteUserName;
	}

	/**
	 * userAttribute
	 * @return the userAttribute
	 */
	
	public String getUserAttribute() {
		return userAttribute;
	}

	/**
	 * @param userAttribute the userAttribute to set
	 */
	
	public void setUserAttribute(String userAttribute) {
		this.userAttribute = userAttribute;
	}

	/**
	 * borrowNid
	 * @return the borrowNid
	 */
	
	public String getBorrowNid() {
		return borrowNid;
	}

	/**
	 * @param borrowNid the borrowNid to set
	 */
	
	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	/**
	 * expectApr
	 * @return the expectApr
	 */
	
	public String getExpectApr() {
		return expectApr;
	}

	/**
	 * @param expectApr the expectApr to set
	 */
	
	public void setExpectApr(String expectApr) {
		this.expectApr = expectApr;
	}

	/**
	 * accedeAccount
	 * @return the accedeAccount
	 */
	
	public String getAccedeAccount() {
		return accedeAccount;
	}

	/**
	 * @param accedeAccount the accedeAccount to set
	 */
	
	public void setAccedeAccount(String accedeAccount) {
		this.accedeAccount = accedeAccount;
	}

	/**
	 * borrowStyle
	 * @return the borrowStyle
	 */
	
	public String getBorrowStyle() {
		return borrowStyle;
	}

	/**
	 * @param borrowStyle the borrowStyle to set
	 */
	
	public void setBorrowStyle(String borrowStyle) {
		this.borrowStyle = borrowStyle;
	}

	/**
	 * investType
	 * @return the investType
	 */
	
	public String getInvestType() {
		return investType;
	}

	/**
	 * @param investType the investType to set
	 */
	
	public void setInvestType(String investType) {
		this.investType = investType;
	}


	/**
	 * addTime
	 * @return the addTime
	 */
	
	public String getAddTime() {
		return addTime;
	}

	/**
	 * @param addTime the addTime to set
	 */
	
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	/**
	 * isMonth
	 * @return the isMonth
	 */
		
	public String getIsMonth() {
		return isMonth;
			
	}

	/**
	 * @param isMonth the isMonth to set
	 */
		
	public void setIsMonth(String isMonth) {
		this.isMonth = isMonth;
			
	}

	/**
	 * countInterestTime
	 * @return the countInterestTime
	 */
		
	public String getCountInterestTime() {
		return countInterestTime;
			
	}

	/**
	 * @param countInterestTime the countInterestTime to set
	 */
		
	public void setCountInterestTime(String countInterestTime) {
		this.countInterestTime = countInterestTime;
			
	}

	/**
	 * borrowPeriod
	 * @return the borrowPeriod
	 */
	
	public String getBorrowPeriod() {
		return borrowPeriod;
	}

	/**
	 * @param borrowPeriod the borrowPeriod to set
	 */
	
	public void setBorrowPeriod(String borrowPeriod) {
		this.borrowPeriod = borrowPeriod;
	}

	/**
	 * date
	 * @return the date
	 */
		
	public String getDate() {
		return date;
			
	}

	/**
	 * @param date the date to set
	 */
		
	public void setDate(String date) {
		this.date = date;
			
	}
}
