package com.hyjf.mybatis.model.customize.web;

import java.io.Serializable;

public class Empinfo implements Serializable{
	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;
	
	/**
	 * 用戶ID
	 */
	private Integer userId;
	
	/**
	 * 姓名
	 */
	private String fullName;
	/**
	 * 性别
	 */
	private Integer sex;
	/**
	 * 手机号码
	 */
	private String mobileNum;
	/**
	 * 身份证号码
	 */
	private String cardId;
	/**
	 * 户籍地址
	 */
	private String domicileAddress;
	/**
	 * 部门ID
	 */
	private String deptId;
	/**
	 * 部门名称
	 */
	private String deptName;
	/**
	 * 现单位职务
	 */
	private String position;
	/**
	 * 入职日期
	 */
	private String entryDate;
	/**
	 * 离职日期
	 */
	private String dimissionDate;
	/**
	 * 员工状态
	 */
	private String empState;
	/**
	 * 汇盈金服平台用户名
	 */
	private String hyjfAccountName;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getMobileNum() {
		return mobileNum;
	}
	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getDomicileAddress() {
		return domicileAddress;
	}
	public void setDomicileAddress(String domicileAddress) {
		this.domicileAddress = domicileAddress;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public String getDimissionDate() {
		return dimissionDate;
	}
	public void setDimissionDate(String dimissionDate) {
		this.dimissionDate = dimissionDate;
	}
	public String getEmpState() {
		return empState;
	}
	public void setEmpState(String empState) {
		this.empState = empState;
	}
	public String getHyjfAccountName() {
		return hyjfAccountName;
	}
	public void setHyjfAccountName(String hyjfAccountName) {
		this.hyjfAccountName = hyjfAccountName;
	}
	@Override
	public String toString() {
		return "Empinfo [fullName=" + fullName + ", sex=" + sex
				+ ", mobileNum=" + mobileNum + ", cardId=" + cardId
				+ ", domicileAddress=" + domicileAddress + ", deptId=" + deptId
				+ ", deptName=" + deptName + ", position=" + position
				+ ", entryDate=" + entryDate + ", dimissionDate="
				+ dimissionDate + ", empState=" + empState
				+ ", hyjfAccountName=" + hyjfAccountName + "]";
	}
	
	

}
