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

public class BorrowCreditCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * 检索条件 用户名
	 */
	private String usernameSrch;

	/**
	 * 检索条件 债转编号
	 */
	private String creditNidSrch;

	/**
	 * 检索条件 项目编号
	 */
	private String bidNidSrch;

	/**
	 * 检索条件 转让状态
	 */
	private String creditStatusSrch;

	/**
	 * 检索条件 发布时间开始
	 */
	private String timeStartSrch;

	/**
	 * 检索条件 发布时间开始
	 */
	private String timeEndSrch;

	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;

	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	/**
	 * 序号
	 */
	private String creditId;

	/**
	 * 债转编号
	 */
	private String creditNid;

	/**
	 * 项目编号
	 */
	private String bidNid;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 债权本金
	 */
	private String creditCapital;

	/**
	 * 转让本金
	 */
	private String creditCapitalPrice;

	/**
	 * 折让率
	 */
	private String creditDiscount;

	/**
	 * 转让价格
	 */
	private String creditPrice;

	/**
	 * 已转让金额
	 */
	private String creditCapitalAssigned;

	/**
	 * 转让状态
	 */
	private String creditStatus;

	/**
	 * 发布时间
	 */
	private String addTime;

	/**
	 * 还款时间
	 */
	private String repayLastTime;

	/**
	 * 订单号
	 */
	private String assignNid;

	/**
	 * 出让人
	 */
	private String creditUsername;

	/**
	 * 转让本金
	 */
	private String assignCapital;

	/**
	 * 转让价格
	 */
	private String assignCapitalPrice;

	/**
	 * 认购金额
	 */
	private String assignPrice;

	/**
	 * 垫付利息
	 */
	private String assignInterestAdvance;
	/**
	 * 服务费
	 */
	private String creditFee;
	/**
	 * 认购时间
	 */
	private String assignPay;
	/**
	 * 状态名称
	 */
	private String creditStatusName;
	/**
	 * 状态名称
	 */
	private String repayStatusName;
	/**
	 * 客户端 0pc 1ios 2Android 3微信
	 */
	private String client;
	
	/**
	 * 承接用户推荐人用户名
	 */
	private String recommendName;
	
	/**
	 * 承接用户推荐人属性
	 */
	private String recommendAttr;
	
	/**
	 * 承接人部门信息
	 */
	private String regionName;
	
	/**
	 * 承接人部门信息
	 */
	private String branchName;
	
	/**
	 * 承接人部门信息
	 */
	private String departmentName;
	
	/**
	 * 出让用户推荐人用户名
	 */
	private String recommendNameCredit;
	
	/**
	 * 出让用户推荐人属性
	 */
	private String recommendAttrCredit;
	
	/**
	 * 出让人部门信息
	 */
	private String regionNameCredit;
	
	/**
	 * 出让人部门信息
	 */
	private String branchNameCredit;
	
	/**
	 * 出让人部门信息
	 */
	private String departmentNameCredit;
	
	/**
	 * 承接人承接时推荐人信息
	 */
	private String inviteUserName;
	private String inviteUserAttribute;
	private String inviteUseRegionname;
	private String inviteUserBranchname;
	private String inviteUserDepartmentName;
	/**
	 * 出让人承接时推荐人信息
	 */
	private String inviteUserCreditName;
	private String inviteUserCreditAttribute;
	private String inviteUserCreditRegionName;
	private String inviteUserCreditBranchName;
	private String inviteUserCreditDepartmentName;

	/**
	 * 承接人用户属性及部门信息
	 */
	private String recommendAttrSelf;
	private String regionNameSelf;
	private String branchNameSelf;
	private String departmentNameSelf;

	/**
	 * 出让人用户属性及部门信息
	 */
	private String recommendAttrCreditSelf;
	private String regionNameCreditSelf;
	private String branchNameCreditSelf;
	private String departmentNameCreditSelf;

	/*------add by LSY START-----------*/
	/**
	 * 金额合计取得信息
	 */
	private String sumCreditCapital;
	private String sumCreditCapitalPrice;
	private String sumCreditPrice;
	private String sumCreditCapitalAssigned;
	private String sumAssignCapital;
	private String sumAssignCapitalPrice;
	private String sumAssignPrice;
	private String sumAssignInterestAdvance;
	private String sumCreditFee;
	private String sumAssignPay;
	/*------add by LSY END-----------*/

	/**
	 * 合同状态
	 */
	private String contractStatus;

	/**
	 * 合同编号
	 */
	private String contractNumber;

	/**
	 * 合同下载地址
	 */
	private String downloadUrl;

	/**
	 * 合同查看地址
	 */
	private String viewpdfUrl;

	/**
	 * 脱敏合同地址
	 */
	private String imgUrl;

	/**
	 * 承接人用户ID
	 */
	private Integer userId;

	/**
	 * 原始出借订单号
	 */
	private String creditTenderNid;

	/**
	 * creditStatusName
	 * 
	 * @return the creditStatusName
	 */

	public String getCreditStatusName() {
		return creditStatusName;
	}

	/**
	 * @param creditStatusName
	 *            the creditStatusName to set
	 */

	public void setCreditStatusName(String creditStatusName) {
		this.creditStatusName = creditStatusName;
	}

	/**
	 * assignCapital
	 * 
	 * @return the assignCapital
	 */

	public String getAssignCapital() {
		return assignCapital;
	}

	/**
	 * @param assignCapital
	 *            the assignCapital to set
	 */

	public void setAssignCapital(String assignCapital) {
		this.assignCapital = assignCapital;
	}

	/**
	 * assignCapitalPrice
	 * 
	 * @return the assignCapitalPrice
	 */

	public String getAssignCapitalPrice() {
		return assignCapitalPrice;
	}

	/**
	 * @param assignCapitalPrice
	 *            the assignCapitalPrice to set
	 */

	public void setAssignCapitalPrice(String assignCapitalPrice) {
		this.assignCapitalPrice = assignCapitalPrice;
	}

	/**
	 * assignNid
	 * 
	 * @return the assignNid
	 */

	public String getAssignNid() {
		return assignNid;
	}

	/**
	 * @param assignNid
	 *            the assignNid to set
	 */

	public void setAssignNid(String assignNid) {
		this.assignNid = assignNid;
	}

	/**
	 * creditUsername
	 * 
	 * @return the creditUsername
	 */

	public String getCreditUsername() {
		return creditUsername;
	}

	/**
	 * @param creditUsername
	 *            the creditUsername to set
	 */

	public void setCreditUsername(String creditUsername) {
		this.creditUsername = creditUsername;
	}

	/**
	 * assignPrice
	 * 
	 * @return the assignPrice
	 */

	public String getAssignPrice() {
		return assignPrice;
	}

	/**
	 * @param assignPrice
	 *            the assignPrice to set
	 */

	public void setAssignPrice(String assignPrice) {
		this.assignPrice = assignPrice;
	}

	/**
	 * assignInterestAdvance
	 * 
	 * @return the assignInterestAdvance
	 */

	public String getAssignInterestAdvance() {
		return assignInterestAdvance;
	}

	/**
	 * @param assignInterestAdvance
	 *            the assignInterestAdvance to set
	 */

	public void setAssignInterestAdvance(String assignInterestAdvance) {
		this.assignInterestAdvance = assignInterestAdvance;
	}

	/**
	 * creditFee
	 * 
	 * @return the creditFee
	 */

	public String getCreditFee() {
		return creditFee;
	}

	/**
	 * @param creditFee
	 *            the creditFee to set
	 */

	public void setCreditFee(String creditFee) {
		this.creditFee = creditFee;
	}

	/**
	 * assignPay
	 * 
	 * @return the assignPay
	 */

	public String getAssignPay() {
		return assignPay;
	}

	/**
	 * @param assignPay
	 *            the assignPay to set
	 */

	public void setAssignPay(String assignPay) {
		this.assignPay = assignPay;
	}

	/**
	 * creditCapitalPrice
	 * 
	 * @return the creditCapitalPrice
	 */

	public String getCreditCapitalPrice() {
		return creditCapitalPrice;
	}

	/**
	 * @param creditCapitalPrice
	 *            the creditCapitalPrice to set
	 */

	public void setCreditCapitalPrice(String creditCapitalPrice) {
		this.creditCapitalPrice = creditCapitalPrice;
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
	 * creditNidSrch
	 * 
	 * @return the creditNidSrch
	 */

	public String getCreditNidSrch() {
		return creditNidSrch;
	}

	/**
	 * @param creditNidSrch
	 *            the creditNidSrch to set
	 */

	public void setCreditNidSrch(String creditNidSrch) {
		this.creditNidSrch = creditNidSrch;
	}

	/**
	 * bidNidSrch
	 * 
	 * @return the bidNidSrch
	 */

	public String getBidNidSrch() {
		return bidNidSrch;
	}

	/**
	 * @param bidNidSrch
	 *            the bidNidSrch to set
	 */

	public void setBidNidSrch(String bidNidSrch) {
		this.bidNidSrch = bidNidSrch;
	}

	/**
	 * creditStatusSrch
	 * 
	 * @return the creditStatusSrch
	 */

	public String getCreditStatusSrch() {
		return creditStatusSrch;
	}

	/**
	 * @param creditStatusSrch
	 *            the creditStatusSrch to set
	 */

	public void setCreditStatusSrch(String creditStatusSrch) {
		this.creditStatusSrch = creditStatusSrch;
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
	 * creditId
	 * 
	 * @return the creditId
	 */

	public String getCreditId() {
		return creditId;
	}

	/**
	 * @param creditId
	 *            the creditId to set
	 */

	public void setCreditId(String creditId) {
		this.creditId = creditId;
	}

	/**
	 * creditNid
	 * 
	 * @return the creditNid
	 */

	public String getCreditNid() {
		return creditNid;
	}

	/**
	 * @param creditNid
	 *            the creditNid to set
	 */

	public void setCreditNid(String creditNid) {
		this.creditNid = creditNid;
	}

	/**
	 * bidNid
	 * 
	 * @return the bidNid
	 */

	public String getBidNid() {
		return bidNid;
	}

	/**
	 * @param bidNid
	 *            the bidNid to set
	 */

	public void setBidNid(String bidNid) {
		this.bidNid = bidNid;
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
	 * creditCapital
	 * 
	 * @return the creditCapital
	 */

	public String getCreditCapital() {
		return creditCapital;
	}

	/**
	 * @param creditCapital
	 *            the creditCapital to set
	 */

	public void setCreditCapital(String creditCapital) {
		this.creditCapital = creditCapital;
	}

	/**
	 * creditDiscount
	 * 
	 * @return the creditDiscount
	 */

	public String getCreditDiscount() {
		return creditDiscount;
	}

	/**
	 * @param creditDiscount
	 *            the creditDiscount to set
	 */

	public void setCreditDiscount(String creditDiscount) {
		this.creditDiscount = creditDiscount;
	}

	/**
	 * creditPrice
	 * 
	 * @return the creditPrice
	 */

	public String getCreditPrice() {
		return creditPrice;
	}

	/**
	 * @param creditPrice
	 *            the creditPrice to set
	 */

	public void setCreditPrice(String creditPrice) {
		this.creditPrice = creditPrice;
	}

	/**
	 * creditCapitalAssigned
	 * 
	 * @return the creditCapitalAssigned
	 */

	public String getCreditCapitalAssigned() {
		return creditCapitalAssigned;
	}

	/**
	 * @param creditCapitalAssigned
	 *            the creditCapitalAssigned to set
	 */

	public void setCreditCapitalAssigned(String creditCapitalAssigned) {
		this.creditCapitalAssigned = creditCapitalAssigned;
	}

	/**
	 * creditStatus
	 * 
	 * @return the creditStatus
	 */

	public String getCreditStatus() {
		return creditStatus;
	}

	/**
	 * @param creditStatus
	 *            the creditStatus to set
	 */

	public void setCreditStatus(String creditStatus) {
		this.creditStatus = creditStatus;
	}

	/**
	 * addTime
	 * 
	 * @return the addTime
	 */

	public String getAddTime() {
		return addTime;
	}

	/**
	 * @param addTime
	 *            the addTime to set
	 */

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getRepayStatusName() {
		return repayStatusName;
	}

	public void setRepayStatusName(String repayStatusName) {
		this.repayStatusName = repayStatusName;
	}

	public String getRecommendName() {
		return recommendName;
	}

	public void setRecommendName(String recommendName) {
		this.recommendName = recommendName;
	}

	public String getRecommendAttr() {
		return recommendAttr;
	}

	public void setRecommendAttr(String recommendAttr) {
		this.recommendAttr = recommendAttr;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getRecommendNameCredit() {
		return recommendNameCredit;
	}

	public void setRecommendNameCredit(String recommendNameCredit) {
		this.recommendNameCredit = recommendNameCredit;
	}

	public String getRecommendAttrCredit() {
		return recommendAttrCredit;
	}

	public void setRecommendAttrCredit(String recommendAttrCredit) {
		this.recommendAttrCredit = recommendAttrCredit;
	}

	public String getRegionNameCredit() {
		return regionNameCredit;
	}

	public void setRegionNameCredit(String regionNameCredit) {
		this.regionNameCredit = regionNameCredit;
	}

	public String getBranchNameCredit() {
		return branchNameCredit;
	}

	public void setBranchNameCredit(String branchNameCredit) {
		this.branchNameCredit = branchNameCredit;
	}

	public String getDepartmentNameCredit() {
		return departmentNameCredit;
	}

	public void setDepartmentNameCredit(String departmentNameCredit) {
		this.departmentNameCredit = departmentNameCredit;
	}

	public String getInviteUserName() {
		return inviteUserName;
	}

	public void setInviteUserName(String inviteUserName) {
		this.inviteUserName = inviteUserName;
	}

	public String getInviteUserAttribute() {
		return inviteUserAttribute;
	}

	public void setInviteUserAttribute(String inviteUserAttribute) {
		this.inviteUserAttribute = inviteUserAttribute;
	}

	public String getInviteUseRegionname() {
		return inviteUseRegionname;
	}

	public void setInviteUseRegionname(String inviteUseRegionname) {
		this.inviteUseRegionname = inviteUseRegionname;
	}

	public String getInviteUserBranchname() {
		return inviteUserBranchname;
	}

	public void setInviteUserBranchname(String inviteUserBranchname) {
		this.inviteUserBranchname = inviteUserBranchname;
	}

	public String getInviteUserDepartmentName() {
		return inviteUserDepartmentName;
	}

	public void setInviteUserDepartmentName(String inviteUserDepartmentName) {
		this.inviteUserDepartmentName = inviteUserDepartmentName;
	}

	public String getInviteUserCreditName() {
		return inviteUserCreditName;
	}

	public void setInviteUserCreditName(String inviteUserCreditName) {
		this.inviteUserCreditName = inviteUserCreditName;
	}

	public String getInviteUserCreditAttribute() {
		return inviteUserCreditAttribute;
	}

	public void setInviteUserCreditAttribute(String inviteUserCreditAttribute) {
		this.inviteUserCreditAttribute = inviteUserCreditAttribute;
	}

	public String getInviteUserCreditRegionName() {
		return inviteUserCreditRegionName;
	}

	public void setInviteUserCreditRegionName(String inviteUserCreditRegionName) {
		this.inviteUserCreditRegionName = inviteUserCreditRegionName;
	}

	public String getInviteUserCreditBranchName() {
		return inviteUserCreditBranchName;
	}

	public void setInviteUserCreditBranchName(String inviteUserCreditBranchName) {
		this.inviteUserCreditBranchName = inviteUserCreditBranchName;
	}

	public String getInviteUserCreditDepartmentName() {
		return inviteUserCreditDepartmentName;
	}

	public void setInviteUserCreditDepartmentName(String inviteUserCreditDepartmentName) {
		this.inviteUserCreditDepartmentName = inviteUserCreditDepartmentName;
	}

	public String getRecommendAttrSelf() {
		return recommendAttrSelf;
	}

	public void setRecommendAttrSelf(String recommendAttrSelf) {
		this.recommendAttrSelf = recommendAttrSelf;
	}

	public String getRegionNameSelf() {
		return regionNameSelf;
	}

	public void setRegionNameSelf(String regionNameSelf) {
		this.regionNameSelf = regionNameSelf;
	}

	public String getBranchNameSelf() {
		return branchNameSelf;
	}

	public void setBranchNameSelf(String branchNameSelf) {
		this.branchNameSelf = branchNameSelf;
	}

	public String getDepartmentNameSelf() {
		return departmentNameSelf;
	}

	public void setDepartmentNameSelf(String departmentNameSelf) {
		this.departmentNameSelf = departmentNameSelf;
	}

	public String getRecommendAttrCreditSelf() {
		return recommendAttrCreditSelf;
	}

	public void setRecommendAttrCreditSelf(String recommendAttrCreditSelf) {
		this.recommendAttrCreditSelf = recommendAttrCreditSelf;
	}

	public String getRegionNameCreditSelf() {
		return regionNameCreditSelf;
	}

	public void setRegionNameCreditSelf(String regionNameCreditSelf) {
		this.regionNameCreditSelf = regionNameCreditSelf;
	}

	public String getBranchNameCreditSelf() {
		return branchNameCreditSelf;
	}

	public void setBranchNameCreditSelf(String branchNameCreditSelf) {
		this.branchNameCreditSelf = branchNameCreditSelf;
	}

	public String getDepartmentNameCreditSelf() {
		return departmentNameCreditSelf;
	}

	public void setDepartmentNameCreditSelf(String departmentNameCreditSelf) {
		this.departmentNameCreditSelf = departmentNameCreditSelf;
	}

	/**
	 * sumCreditCapital
	 * @return the sumCreditCapital
	 */
		
	public String getSumCreditCapital() {
		return sumCreditCapital;
			
	}

	/**
	 * @param sumCreditCapital the sumCreditCapital to set
	 */
		
	public void setSumCreditCapital(String sumCreditCapital) {
		this.sumCreditCapital = sumCreditCapital;
			
	}

	/**
	 * sumCreditCapitalPrice
	 * @return the sumCreditCapitalPrice
	 */
		
	public String getSumCreditCapitalPrice() {
		return sumCreditCapitalPrice;
			
	}

	/**
	 * @param sumCreditCapitalPrice the sumCreditCapitalPrice to set
	 */
		
	public void setSumCreditCapitalPrice(String sumCreditCapitalPrice) {
		this.sumCreditCapitalPrice = sumCreditCapitalPrice;
			
	}

	/**
	 * sumCreditPrice
	 * @return the sumCreditPrice
	 */
		
	public String getSumCreditPrice() {
		return sumCreditPrice;
			
	}

	/**
	 * @param sumCreditPrice the sumCreditPrice to set
	 */
		
	public void setSumCreditPrice(String sumCreditPrice) {
		this.sumCreditPrice = sumCreditPrice;
			
	}

	/**
	 * sumCreditCapitalAssigned
	 * @return the sumCreditCapitalAssigned
	 */
		
	public String getSumCreditCapitalAssigned() {
		return sumCreditCapitalAssigned;
			
	}

	/**
	 * @param sumCreditCapitalAssigned the sumCreditCapitalAssigned to set
	 */
		
	public void setSumCreditCapitalAssigned(String sumCreditCapitalAssigned) {
		this.sumCreditCapitalAssigned = sumCreditCapitalAssigned;
			
	}

	/**
	 * sumAssignCapital
	 * @return the sumAssignCapital
	 */
		
	public String getSumAssignCapital() {
		return sumAssignCapital;
			
	}

	/**
	 * @param sumAssignCapital the sumAssignCapital to set
	 */
		
	public void setSumAssignCapital(String sumAssignCapital) {
		this.sumAssignCapital = sumAssignCapital;
			
	}

	/**
	 * sumAssignCapitalPrice
	 * @return the sumAssignCapitalPrice
	 */
		
	public String getSumAssignCapitalPrice() {
		return sumAssignCapitalPrice;
			
	}

	/**
	 * @param sumAssignCapitalPrice the sumAssignCapitalPrice to set
	 */
		
	public void setSumAssignCapitalPrice(String sumAssignCapitalPrice) {
		this.sumAssignCapitalPrice = sumAssignCapitalPrice;
			
	}

	/**
	 * sumAssignPrice
	 * @return the sumAssignPrice
	 */
		
	public String getSumAssignPrice() {
		return sumAssignPrice;
			
	}

	/**
	 * @param sumAssignPrice the sumAssignPrice to set
	 */
		
	public void setSumAssignPrice(String sumAssignPrice) {
		this.sumAssignPrice = sumAssignPrice;
			
	}

	/**
	 * sumAssignInterestAdvance
	 * @return the sumAssignInterestAdvance
	 */
		
	public String getSumAssignInterestAdvance() {
		return sumAssignInterestAdvance;
			
	}

	/**
	 * @param sumAssignInterestAdvance the sumAssignInterestAdvance to set
	 */
		
	public void setSumAssignInterestAdvance(String sumAssignInterestAdvance) {
		this.sumAssignInterestAdvance = sumAssignInterestAdvance;
			
	}

	/**
	 * sumCreditFee
	 * @return the sumCreditFee
	 */
		
	public String getSumCreditFee() {
		return sumCreditFee;
			
	}

	/**
	 * @param sumCreditFee the sumCreditFee to set
	 */
		
	public void setSumCreditFee(String sumCreditFee) {
		this.sumCreditFee = sumCreditFee;
			
	}

	/**
	 * sumAssignPay
	 * @return the sumAssignPay
	 */
		
	public String getSumAssignPay() {
		return sumAssignPay;
			
	}

	/**
	 * @param sumAssignPay the sumAssignPay to set
	 */
		
	public void setSumAssignPay(String sumAssignPay) {
		this.sumAssignPay = sumAssignPay;
			
	}

	public String getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getViewpdfUrl() {
		return viewpdfUrl;
	}

	public void setViewpdfUrl(String viewpdfUrl) {
		this.viewpdfUrl = viewpdfUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCreditTenderNid() {
		return creditTenderNid;
	}

	public void setCreditTenderNid(String creditTenderNid) {
		this.creditTenderNid = creditTenderNid;
	}
}
