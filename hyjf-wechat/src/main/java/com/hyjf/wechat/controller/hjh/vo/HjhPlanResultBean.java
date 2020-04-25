package com.hyjf.wechat.controller.hjh.vo;

/**
 * @author xiasq
 * @version HjhPlanResultBean, v0.1 2017/12/7 13:45
 * 计划详细信息响应
 */
public class HjhPlanResultBean {

	// 用户验证 出借用
	private UserValidation userValidation;
	// 计划基本信息
	private ProjectInfo projectInfo;
	// 计划描述的一些信息: 计划介绍 加入条件
	private ProjectDetail projectDetail;

	public HjhPlanResultBean() {
		super();
		this.userValidation = new UserValidation();
		this.projectInfo = new ProjectInfo();
		this.projectDetail = new ProjectDetail();
	}

	/**
	 * 用户验证
	 */
	public static final class UserValidation {

		// 是否登录
		private Boolean isLogined;
		// 是否开户
		private Boolean isOpened;
		// 是否设置交易密码
		private Boolean isSetPassword;
		// 是否允许使用
		private Boolean isAllowed;
		// 是否完成风险测评
		private String isRiskTested;
		// 是否出借授权(投标授权)
		private Boolean isAutoInves;
		// 是否债转授权
		private Boolean isAutoTransfer;
		// 是否缴费授权
		private Integer paymentAuthStatus;
		// 缴费授权开关
        private Integer paymentAuthOn;
        // 自动出借授权开关
        private Integer invesAuthOn;
        // 自动债转授权开关
        private Integer creditAuthOn;
        //角色认证是否打开,true认证,false不认证
        private Boolean isCheckUserRole;
		
        private Integer roleId;//角色id
		public Boolean getLogined() {
			return isLogined;
		}

		public void setLogined(Boolean logined) {
			isLogined = logined;
		}

		public Boolean getOpened() {
			return isOpened;
		}

		public void setOpened(Boolean opened) {
			isOpened = opened;
		}

		public Boolean getSetPassword() {
			return isSetPassword;
		}

		public void setSetPassword(Boolean setPassword) {
			isSetPassword = setPassword;
		}

		public Boolean getAllowed() {
			return isAllowed;
		}

		public void setAllowed(Boolean allowed) {
			isAllowed = allowed;
		}

		public String getRiskTested() {
			return isRiskTested;
		}

		public void setRiskTested(String riskTested) {
			isRiskTested = riskTested;
		}

		public Boolean getAutoInves() {
			return isAutoInves;
		}

		public void setAutoInves(Boolean autoInves) {
			isAutoInves = autoInves;
		}

		public Boolean getAutoTransfer() {
			return isAutoTransfer;
		}

		public void setAutoTransfer(Boolean autoTransfer) {
			isAutoTransfer = autoTransfer;
		}

		
		public Integer getPaymentAuthStatus() {
			return paymentAuthStatus;
		}

		public void setPaymentAuthStatus(Integer paymentAuthStatus) {
			this.paymentAuthStatus = paymentAuthStatus;
		}

		public Integer getRoleId() {
			return roleId;
		}

		public void setRoleId(Integer roleId) {
			this.roleId = roleId;
		}

		public Integer getPaymentAuthOn() {
            return paymentAuthOn;
        }

        public void setPaymentAuthOn(Integer paymentAuthOn) {
            this.paymentAuthOn = paymentAuthOn;
        }

		public Boolean getIsCheckUserRole() {
			return isCheckUserRole;
		}

		public void setIsCheckUserRole(Boolean isCheckUserRole) {
			this.isCheckUserRole = isCheckUserRole;
		}

		public Integer getInvesAuthOn() {
			return invesAuthOn;
		}

		public void setInvesAuthOn(Integer invesAuthOn) {
			this.invesAuthOn = invesAuthOn;
		}

		public Integer getCreditAuthOn() {
			return creditAuthOn;
		}

		public void setCreditAuthOn(Integer creditAuthOn) {
			this.creditAuthOn = creditAuthOn;
		}
		
		
	}

	/**
	 * 计划基本信息
	 */
	public static final class ProjectInfo {
		// 项目类型 :HJH
		private String type;
		// 项目进行状态 如 1：复审中
		private String status;
		// 预期年化利率: 6.5
		private String planApr;
		// 计划期限
		private String planPeriod;
		// 计划期限单位  天 月
		private String planPeriodUnit;
		// 计划加入人次
		private String planPersonTime;
		// 开放额度
		private String account;
		private String planProgressStatus;
		// 计划名称
		private String planName;
		// 计息时间
		private String onAccrual;
		// 还款方式: 按天计息，到期还本还息
		private String repayStyle;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getPlanApr() {
			return planApr;
		}

		public void setPlanApr(String planApr) {
			this.planApr = planApr;
		}

		public String getPlanPeriod() {
			return planPeriod;
		}

		public void setPlanPeriod(String planPeriod) {
			this.planPeriod = planPeriod;
		}

		public String getPlanPeriodUnit() {
			return planPeriodUnit;
		}

		public void setPlanPeriodUnit(String planPeriodUnit) {
			this.planPeriodUnit = planPeriodUnit;
		}

		public String getPlanPersonTime() {
			return planPersonTime;
		}

		public void setPlanPersonTime(String planPersonTime) {
			this.planPersonTime = planPersonTime;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public String getPlanProgressStatus() {
			return planProgressStatus;
		}

		public void setPlanProgressStatus(String planProgressStatus) {
			this.planProgressStatus = planProgressStatus;
		}

		public String getPlanName() {
			return planName;
		}

		public void setPlanName(String planName) {
			this.planName = planName;
		}

		public String getOnAccrual() {
			return onAccrual;
		}

		public void setOnAccrual(String onAccrual) {
			this.onAccrual = onAccrual;
		}

		public String getRepayStyle() {
			return repayStyle;
		}

		public void setRepayStyle(String repayStyle) {
			this.repayStyle = repayStyle;
		}
	}

	/**
	 * 计划描述
	 */
	public static final class ProjectDetail {
		// 计划介绍
		private String planInfo;

		// 加入条件
		private String addCondition;

		public String getPlanInfo() {
			return planInfo;
		}

		public void setPlanInfo(String planInfo) {
			this.planInfo = planInfo;
		}

		public String getAddCondition() {
			return addCondition;
		}

		public void setAddCondition(String addCondition) {
			this.addCondition = addCondition;
		}
	}

	public UserValidation getUserValidation() {
		return userValidation;
	}

	public void setUserValidation(UserValidation userValidation) {
		this.userValidation = userValidation;
	}

	public ProjectInfo getProjectInfo() {
		return projectInfo;
	}

	public void setProjectInfo(ProjectInfo projectInfo) {
		this.projectInfo = projectInfo;
	}

	public ProjectDetail getProjectDetail() {
		return projectDetail;
	}

	public void setProjectDetail(ProjectDetail projectDetail) {
		this.projectDetail = projectDetail;
	}
}
