package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 汇添金提成管理
 * 
 * @ClassName AdminPlanPushMoneyCustomize
 * @author liuyang
 * @date 2016年10月24日 上午11:55:18
 */
public class AdminPlanPushMoneyDetailCustomize implements Serializable {

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 5394683127045219535L;

	/**
	 * 提成ID
	 */
	private String id;

	/**
	 * 提成人
	 */
	private String userName;

	/**
	 * 提成金额
	 */
	private BigDecimal commission;

	/**
	 * 提成转账订单号
	 */
	private String orderId;

	/**
	 * 提成发放时间
	 */
	private String returnTime;

	/**
	 * 分公司
	 */
	private String regionName;

	/**
	 * 分部
	 */
	private String branchName;

	/**
	 * 团队
	 */
	private String departmentName;

	/**
	 * 用户属性
	 */
	private String attribute;

	/**
	 * 出借人
	 */
	private String accedeUserName;

	/**
	 * 出借金额
	 */
	private BigDecimal accedeAccount;

	/**
	 * 计划编号
	 */
	private String debtPlanNid;

	/**
	 * 加入订单号
	 */
	private String accedeOrderId;

	/**
	 * 计划期限
	 */
	private String debtLockPeriod;

	/**
	 * 加入时间
	 */
	private Integer accedeTime;

	/**
	 * 发放状态
	 */
	private String status;

	/**
	 * 是否是51老用户
	 */
	private String is51;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BigDecimal getCommission() {
		return commission;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
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

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getAccedeUserName() {
		return accedeUserName;
	}

	public void setAccedeUserName(String accedeUserName) {
		this.accedeUserName = accedeUserName;
	}

	public BigDecimal getAccedeAccount() {
		return accedeAccount;
	}

	public void setAccedeAccount(BigDecimal accedeAccount) {
		this.accedeAccount = accedeAccount;
	}

	public String getDebtPlanNid() {
		return debtPlanNid;
	}

	public void setDebtPlanNid(String debtPlanNid) {
		this.debtPlanNid = debtPlanNid;
	}

	public String getAccedeOrderId() {
		return accedeOrderId;
	}

	public void setAccedeOrderId(String accedeOrderId) {
		this.accedeOrderId = accedeOrderId;
	}

	public String getDebtLockPeriod() {
		return debtLockPeriod;
	}

	public void setDebtLockPeriod(String debtLockPeriod) {
		this.debtLockPeriod = debtLockPeriod;
	}

	public Integer getAccedeTime() {
		return accedeTime;
	}

	public void setAccedeTime(Integer accedeTime) {
		this.accedeTime = accedeTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIs51() {
		return is51;
	}

	public void setIs51(String is51) {
		this.is51 = is51;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
