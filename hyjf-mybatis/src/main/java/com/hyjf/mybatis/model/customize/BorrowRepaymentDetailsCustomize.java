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
 * 还款明细
 * 
 * @author 孙亮
 * @since 2015年12月19日 上午9:29:09
 */
public class BorrowRepaymentDetailsCustomize implements Serializable {
	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;
	/**
	 * 应还日期 检索条件
	 */
	private String recoverTimeStartSrch;
	
	/**
     * 应还日期 检索条件
     */
    private String recoverTimeEndSrch;
    /**
     * 借款编号 检索条件
     */
    private String borrowNidSrch;
    /**
     * 借款人 检索条件
     */
    private String borrowUserNameSrch;
    /**
     * 出借人 检索条件
     */
    private String recoverUserNameSrch;
    /**
     * 还款状态 检索条件
     */
    private String statusSrch;
    /**
     * 实际回款时间 检索条件
     */
    private String recoverYesTimeStartSrch;
    
    /**
     * 实际回款时间结束 检索条件
     */
    private String recoverYesTimeEndSrch;
    
    /**
     * 推荐人 检索条件
     */
    private String inviteUserNameSrch;
    
    /**
     * 分公司 检索条件
     */
    private String recoverDepartmentNameSrch;
    

	// ========================参数=============================
    private String id;//项目id
    private String borrowNid;//项目编号 
    private String borrowUserId;//借款人id
    private String borrowUserName;//借款人名称
    private String projectTypeName;//项目类别名称
    private String borrowPeriod;//项目期限 
    private String borrowApr;//年化利率
    private String borrowAccount;//借款金额
    private String borrowAccountYes;//借到金额
    private String repayType;//还款类型
    private String recoverUserId;//出借人编号
    private String recoverUserName;//出借人名称
    private String tenderUserAttribute;//出借人属性（出借时）
    private String recoverRegionName;//出借人所属一级分部（当前）
    private String recoverBranchName;//出借人所属二级分部（当前）
    private String recoverDepartmentName;//出借人所属团队（当前）
    private String inviteUserAttribute;//推荐人属性（出借时）
    private String inviteUserName;//推荐人用户名（出借时）
    private String inviteTrueName;//推荐人姓名（出借时）
    private String inviteRegionName;//推荐人所属一级分部（出借时）
    private String inviteBranchName;//推荐人所属二级分部（当出借时）
    private String inviteDepartmentName;//推荐人所属团队（出借时）
    private String recoverPeriod;//分期还款期数
    private String recoverCapital;//应还本金
    private String recoverInterest;//应还利息
    private String recoverAccount;//应还本息
    private String recoverFee;//服务费 
    private String status;//还款状态
    private String recoverTime;//应回款时间 
    private String orderByRecoverTime;//应回款时间戳
    private String recoverYestime;//实际还款时间
    private String orderByRecoverYestime;//实际还款时间戳
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
    public String getRecoverTimeStartSrch() {
        return recoverTimeStartSrch;
    }
    public void setRecoverTimeStartSrch(String recoverTimeStartSrch) {
        this.recoverTimeStartSrch = recoverTimeStartSrch;
    }
    public String getRecoverTimeEndSrch() {
        return recoverTimeEndSrch;
    }
    public void setRecoverTimeEndSrch(String recoverTimeEndSrch) {
        this.recoverTimeEndSrch = recoverTimeEndSrch;
    }
    public String getBorrowNidSrch() {
        return borrowNidSrch;
    }
    public void setBorrowNidSrch(String borrowNidSrch) {
        this.borrowNidSrch = borrowNidSrch;
    }
    public String getBorrowUserNameSrch() {
        return borrowUserNameSrch;
    }
    public void setBorrowUserNameSrch(String borrowUserNameSrch) {
        this.borrowUserNameSrch = borrowUserNameSrch;
    }
    public String getRecoverUserNameSrch() {
        return recoverUserNameSrch;
    }
    public void setRecoverUserNameSrch(String recoverUserNameSrch) {
        this.recoverUserNameSrch = recoverUserNameSrch;
    }
    public String getStatusSrch() {
        return statusSrch;
    }
    public void setStatusSrch(String statusSrch) {
        this.statusSrch = statusSrch;
    }
    public String getRecoverYesTimeStartSrch() {
        return recoverYesTimeStartSrch;
    }
    public void setRecoverYesTimeStartSrch(String recoverYesTimeStartSrch) {
        this.recoverYesTimeStartSrch = recoverYesTimeStartSrch;
    }
    public String getRecoverYesTimeEndSrch() {
        return recoverYesTimeEndSrch;
    }
    public void setRecoverYesTimeEndSrch(String recoverYesTimeEndSrch) {
        this.recoverYesTimeEndSrch = recoverYesTimeEndSrch;
    }
    public String getInviteUserNameSrch() {
        return inviteUserNameSrch;
    }
    public void setInviteUserNameSrch(String inviteUserNameSrch) {
        this.inviteUserNameSrch = inviteUserNameSrch;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBorrowNid() {
        return borrowNid;
    }
    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }
    public String getBorrowUserId() {
        return borrowUserId;
    }
    public void setBorrowUserId(String borrowUserId) {
        this.borrowUserId = borrowUserId;
    }
    public String getBorrowUserName() {
        return borrowUserName;
    }
    public void setBorrowUserName(String borrowUserName) {
        this.borrowUserName = borrowUserName;
    }
    public String getProjectTypeName() {
        return projectTypeName;
    }
    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }
    public String getBorrowPeriod() {
        return borrowPeriod;
    }
    public void setBorrowPeriod(String borrowPeriod) {
        this.borrowPeriod = borrowPeriod;
    }
    public String getBorrowApr() {
        return borrowApr;
    }
    public void setBorrowApr(String borrowApr) {
        this.borrowApr = borrowApr;
    }
    public String getBorrowAccount() {
        return borrowAccount;
    }
    public void setBorrowAccount(String borrowAccount) {
        this.borrowAccount = borrowAccount;
    }
    public String getBorrowAccountYes() {
        return borrowAccountYes;
    }
    public void setBorrowAccountYes(String borrowAccountYes) {
        this.borrowAccountYes = borrowAccountYes;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public String getRecoverUserId() {
        return recoverUserId;
    }
    public void setRecoverUserId(String recoverUserId) {
        this.recoverUserId = recoverUserId;
    }
    public String getRecoverUserName() {
        return recoverUserName;
    }
    public void setRecoverUserName(String recoverUserName) {
        this.recoverUserName = recoverUserName;
    }
    public String getTenderUserAttribute() {
        return tenderUserAttribute;
    }
    public void setTenderUserAttribute(String tenderUserAttribute) {
        this.tenderUserAttribute = tenderUserAttribute;
    }
    public String getRecoverRegionName() {
        return recoverRegionName;
    }
    public void setRecoverRegionName(String recoverRegionName) {
        this.recoverRegionName = recoverRegionName;
    }
    public String getRecoverBranchName() {
        return recoverBranchName;
    }
    public void setRecoverBranchName(String recoverBranchName) {
        this.recoverBranchName = recoverBranchName;
    }
    public String getRecoverDepartmentName() {
        return recoverDepartmentName;
    }
    public void setRecoverDepartmentName(String recoverDepartmentName) {
        this.recoverDepartmentName = recoverDepartmentName;
    }
    public String getInviteUserAttribute() {
        return inviteUserAttribute;
    }
    public void setInviteUserAttribute(String inviteUserAttribute) {
        this.inviteUserAttribute = inviteUserAttribute;
    }
    public String getInviteUserName() {
        return inviteUserName;
    }
    public void setInviteUserName(String inviteUserName) {
        this.inviteUserName = inviteUserName;
    }
    public String getInviteRegionName() {
        return inviteRegionName;
    }
    public void setInviteRegionName(String inviteRegionName) {
        this.inviteRegionName = inviteRegionName;
    }
    public String getInviteBranchName() {
        return inviteBranchName;
    }
    public void setInviteBranchName(String inviteBranchName) {
        this.inviteBranchName = inviteBranchName;
    }
    public String getInviteDepartmentName() {
        return inviteDepartmentName;
    }
    public void setInviteDepartmentName(String inviteDepartmentName) {
        this.inviteDepartmentName = inviteDepartmentName;
    }
    public String getRecoverPeriod() {
        return recoverPeriod;
    }
    public void setRecoverPeriod(String recoverPeriod) {
        this.recoverPeriod = recoverPeriod;
    }
    public String getRecoverCapital() {
        return recoverCapital;
    }
    public void setRecoverCapital(String recoverCapital) {
        this.recoverCapital = recoverCapital;
    }
    public String getRecoverInterest() {
        return recoverInterest;
    }
    public void setRecoverInterest(String recoverInterest) {
        this.recoverInterest = recoverInterest;
    }
    public String getRecoverAccount() {
        return recoverAccount;
    }
    public void setRecoverAccount(String recoverAccount) {
        this.recoverAccount = recoverAccount;
    }
    public String getRecoverFee() {
        return recoverFee;
    }
    public void setRecoverFee(String recoverFee) {
        this.recoverFee = recoverFee;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getRecoverTime() {
        return recoverTime;
    }
    public void setRecoverTime(String recoverTime) {
        this.recoverTime = recoverTime;
    }
    public String getOrderByRecoverTime() {
        return orderByRecoverTime;
    }
    public void setOrderByRecoverTime(String orderByRecoverTime) {
        this.orderByRecoverTime = orderByRecoverTime;
    }
    public String getRecoverYestime() {
        return recoverYestime;
    }
    public void setRecoverYestime(String recoverYestime) {
        this.recoverYestime = recoverYestime;
    }
    public String getOrderByRecoverYestime() {
        return orderByRecoverYestime;
    }
    public void setOrderByRecoverYestime(String orderByRecoverYestime) {
        this.orderByRecoverYestime = orderByRecoverYestime;
    }
    public String getRecoverDepartmentNameSrch() {
        return recoverDepartmentNameSrch;
    }
    public void setRecoverDepartmentNameSrch(String recoverDepartmentNameSrch) {
        this.recoverDepartmentNameSrch = recoverDepartmentNameSrch;
    }
    public String getInviteTrueName() {
        return inviteTrueName;
    }
    public void setInviteTrueName(String inviteTrueName) {
        this.inviteTrueName = inviteTrueName;
    }
    
    
    
}
