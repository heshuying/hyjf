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

package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

/**
 * @author Administrator
 */

public class BorrowFullCustomize implements Serializable {

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
	
	/**项目复审状态*/
	private String reverifyStatus;
	
	/**项目复审状态*/
	private String reverifyStatusName;
	
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
	 * 项目类型名称
	 */
	private String projectTypeName;
	/**
	 * 项目类型名称
	 */
	private String managerScaleEnd;
	/**
	 * 爆标标识
	 */
	private String accountFlag;
	
	/*------add by LSY START-------------*/
	/**
     * 借款金额合计
     */
    private String sumAccount;
    
	/**
	 * 借到金额合计
	 */
	private String sumBorrowAccountYes;
	
	/**
	 * 服务费合计
	 */
	private String sumServiceScale;
	/*------add by LSY END-------------*/

	// 复审中的列表

	// 出借人
	private String investor;
	// 出借金额（元）
	private String investmentAmount;
	// 应放款金额
	private String loanAmount;
	// 应收服务费
	private String serviceCharge;
	// 操作平台
	private String operatingDeck;
	// 操作时间
	private String operatingTime;
	
	// 是否使用引擎 0:否 , 1：是
	private String isEngineUsed;
	
	// 复审中的列表

	/**
	 * managerScaleEnd
	 * 
	 * @return the managerScaleEnd
	 */

	public String getManagerScaleEnd() {
		return managerScaleEnd;
	}

	/**
	 * accountFlag
	 * 
	 * @return the accountFlag
	 */

	public String getAccountFlag() {
		return accountFlag;
	}

	/**
	 * @param accountFlag
	 *            the accountFlag to set
	 */

	public void setAccountFlag(String accountFlag) {
		this.accountFlag = accountFlag;
	}

	/**
	 * @param managerScaleEnd
	 *            the managerScaleEnd to set
	 */

	public void setManagerScaleEnd(String managerScaleEnd) {
		this.managerScaleEnd = managerScaleEnd;
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
	 * investor
	 * 
	 * @return the investor
	 */

	public String getInvestor() {
		return investor;
	}

	/**
	 * @param investor
	 *            the investor to set
	 */

	public void setInvestor(String investor) {
		this.investor = investor;
	}

	/**
	 * investmentAmount
	 * 
	 * @return the investmentAmount
	 */

	public String getInvestmentAmount() {
		return investmentAmount;
	}

	/**
	 * @param investmentAmount
	 *            the investmentAmount to set
	 */

	public void setInvestmentAmount(String investmentAmount) {
		this.investmentAmount = investmentAmount;
	}

	/**
	 * loanAmount
	 * 
	 * @return the loanAmount
	 */

	public String getLoanAmount() {
		return loanAmount;
	}

	/**
	 * @param loanAmount
	 *            the loanAmount to set
	 */

	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}

	/**
	 * serviceCharge
	 * 
	 * @return the serviceCharge
	 */

	public String getServiceCharge() {
		return serviceCharge;
	}

	/**
	 * @param serviceCharge
	 *            the serviceCharge to set
	 */

	public void setServiceCharge(String serviceCharge) {
		this.serviceCharge = serviceCharge;
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
	 * operatingTime
	 * 
	 * @return the operatingTime
	 */

	public String getOperatingTime() {
		return operatingTime;
	}

	/**
	 * @param operatingTime
	 *            the operatingTime to set
	 */

	public void setOperatingTime(String operatingTime) {
		this.operatingTime = operatingTime;
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

	public String getReverifyStatus() {
		return reverifyStatus;
	}

	public void setReverifyStatus(String reverifyStatus) {
		this.reverifyStatus = reverifyStatus;
	}

	public String getReverifyStatusName() {
		return reverifyStatusName;
	}

	public void setReverifyStatusName(String reverifyStatusName) {
		this.reverifyStatusName = reverifyStatusName;
	}

	/**
	 * sumAccount
	 * @return the sumAccount
	 */
		
	public String getSumAccount() {
		return sumAccount;
			
	}

	/**
	 * @param sumAccount the sumAccount to set
	 */
		
	public void setSumAccount(String sumAccount) {
		this.sumAccount = sumAccount;
			
	}

	/**
	 * sumBorrowAccountYes
	 * @return the sumBorrowAccountYes
	 */
		
	public String getSumBorrowAccountYes() {
		return sumBorrowAccountYes;
			
	}

	/**
	 * @param sumBorrowAccountYes the sumBorrowAccountYes to set
	 */
		
	public void setSumBorrowAccountYes(String sumBorrowAccountYes) {
		this.sumBorrowAccountYes = sumBorrowAccountYes;
			
	}

	/**
	 * sumServiceScale
	 * @return the sumServiceScale
	 */
		
	public String getSumServiceScale() {
		return sumServiceScale;
			
	}

	/**
	 * @param sumServiceScale the sumServiceScale to set
	 */
		
	public void setSumServiceScale(String sumServiceScale) {
		this.sumServiceScale = sumServiceScale;
			
	}

	public String getIsEngineUsed() {
		return isEngineUsed;
	}

	public void setIsEngineUsed(String isEngineUsed) {
		this.isEngineUsed = isEngineUsed;
	}
	
	

}
