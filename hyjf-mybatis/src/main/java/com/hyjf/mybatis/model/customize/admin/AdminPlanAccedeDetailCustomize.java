package com.hyjf.mybatis.model.customize.admin;

/**
 * 汇添金加入明细
 * 
 * @ClassName AdminPlanAccedeDetailCustomize
 * @author liuyang
 * @date 2016年9月29日 上午10:19:06
 */
public class AdminPlanAccedeDetailCustomize {
	/**
	 * 计划编号
	 */
	private String debtPlanNid;

	/**
	 * 计划名称
	 */
	private String debtPlanName;
	/**
	 * 计划类型名称
	 */
	private String debtPlanTypeName;

	/**
	 * 加入订单号
	 */
	private String planOrderId;

	/**
	 * 冻结订单号
	 */
	private String freezeOrderId;

	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 推荐人
	 */
	private String refereeUserName;

	/**
	 * 用户属性
	 */
	private String userAttribute;

	/**
	 * 项目期限
	 */
	private String debtLockPeriod;

	/**
	 * 预期年化收益
	 */
	private String expectApr;

	/**
	 * 加入金额
	 */
	private String accedeAccount;

	/**
	 * 状态
	 */
	private String debtPlanStatus;

	/**
	 * 平台
	 */
	private String platform;

	/**
	 * 加入时间
	 */
	private String createTime;

	/**
	 * 加入时推荐人
	 */
	private String inviteUserName;

	/**
	 * 加入时分公司
	 */
	private String inviteRegionName;

	/**
	 * 加入时部门
	 */
	private String inviteBranchName;
	/**
	 * 加入时平台
	 */
	private String client;

	/**
	 * 加入是团队
	 */
	private String inviteDepartmentName;
	
	private String tenderType;
	
	private String couponCode;
	
	public String getDebtPlanNid() {
		return debtPlanNid;
	}

	public void setDebtPlanNid(String debtPlanNid) {
		this.debtPlanNid = debtPlanNid;
	}

	
	public String getDebtPlanName() {
		return debtPlanName;
	}

	public void setDebtPlanName(String debtPlanName) {
		this.debtPlanName = debtPlanName;
	}

	public String getPlanOrderId() {
		return planOrderId;
	}

	public void setPlanOrderId(String planOrderId) {
		this.planOrderId = planOrderId;
	}

	public String getFreezeOrderId() {
		return freezeOrderId;
	}

	public void setFreezeOrderId(String freezeOrderId) {
		this.freezeOrderId = freezeOrderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRefereeUserName() {
		return refereeUserName;
	}

	public void setRefereeUserName(String refereeUserName) {
		this.refereeUserName = refereeUserName;
	}

	public String getUserAttribute() {
		return userAttribute;
	}

	public void setUserAttribute(String userAttribute) {
		this.userAttribute = userAttribute;
	}

	public String getDebtLockPeriod() {
		return debtLockPeriod;
	}

	public void setDebtLockPeriod(String debtLockPeriod) {
		this.debtLockPeriod = debtLockPeriod;
	}

	public String getExpectApr() {
		return expectApr;
	}

	public void setExpectApr(String expectApr) {
		this.expectApr = expectApr;
	}

	public String getAccedeAccount() {
		return accedeAccount;
	}

	public void setAccedeAccount(String accedeAccount) {
		this.accedeAccount = accedeAccount;
	}

	public String getDebtPlanStatus() {
		return debtPlanStatus;
	}

	public void setDebtPlanStatus(String debtPlanStatus) {
		this.debtPlanStatus = debtPlanStatus;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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

	public String getDebtPlanTypeName() {
		return debtPlanTypeName;
	}

	public void setDebtPlanTypeName(String debtPlanTypeName) {
		this.debtPlanTypeName = debtPlanTypeName;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

    public String getTenderType() {
        return tenderType;
    }

    public void setTenderType(String tenderType) {
        this.tenderType = tenderType;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

}
