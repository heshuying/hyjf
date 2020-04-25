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

package com.hyjf.mybatis.model.customize.admin.htj;

import java.io.Serializable;

/**
 * @author Administrator
 */

public class DebtCreditTenderCustomize implements Serializable {

	/**
	 * serialVersionUID:序列化id
	 */
		
	private static final long serialVersionUID = -7215139114727210609L;

	/**
	 * 主键
	 */
	private String id;
	
	/**
	 * 承接人
	 */
	private String assignUserName;
	
	/**
	 * 承接计划编号
	 */
	private String assignPlanNid;
	
	/**
	 * 承接计划订单号
	 */
	private String assignPlanOrderId;
	
	/**
	 * 承接订单号
	 */
	private String assignOrderId;
	
	/**
	 * 出让人用户名
	 */
	private String creditUserName;
	
	/**
	 * 债转编号
	 */
	private String creditNid;

	/**
	 * 项目编号
	 */
	private String borrowNid;
	
	/**
	 * 还款方式
	 */
	private String repayStyleName;

	/**
	 * 承接本金
	 */
	private String assignCapital;
	
	/**
	 * 垫付利息
	 */
	private String assignInterestAdvance;
	
	/**
	 * 实际支付金额
	 */
	private String assignPay;
	
	/**
	 * 清算手续费率
	 */
	private String serviceFeeRate;
	
	/**
	 * 实际服务费
	 */
	private String serviceFee;
	
	/**
	 * 承接类型
	 */
	private String assignTypeName;

	/**
	 * 承接时间
	 */
	private String assignTime;

	/**
	 * 项目总期数
	 */
	private String borrowPeriod;

	/**
	 * 承接时所在期数
	 */
	private String assignPeriod;
	
	/**
	 * 构造方法
	 */
	public DebtCreditTenderCustomize() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAssignUserName() {
		return assignUserName;
	}

	public void setAssignUserName(String assignUserName) {
		this.assignUserName = assignUserName;
	}

	public String getAssignPlanNid() {
		return assignPlanNid;
	}

	public void setAssignPlanNid(String assignPlanNid) {
		this.assignPlanNid = assignPlanNid;
	}

	public String getAssignPlanOrderId() {
		return assignPlanOrderId;
	}

	public void setAssignPlanOrderId(String assignPlanOrderId) {
		this.assignPlanOrderId = assignPlanOrderId;
	}

	public String getAssignOrderId() {
		return assignOrderId;
	}

	public void setAssignOrderId(String assignOrderId) {
		this.assignOrderId = assignOrderId;
	}
	
	public String getCreditUserName() {
		return creditUserName;
	}

	public void setCreditUserName(String creditUserName) {
		this.creditUserName = creditUserName;
	}

	public String getCreditNid() {
		return creditNid;
	}

	public void setCreditNid(String creditNid) {
		this.creditNid = creditNid;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public String getRepayStyleName() {
		return repayStyleName;
	}

	public void setRepayStyleName(String repayStyleName) {
		this.repayStyleName = repayStyleName;
	}

	public String getAssignCapital() {
		return assignCapital;
	}

	public void setAssignCapital(String assignCapital) {
		this.assignCapital = assignCapital;
	}

	public String getAssignInterestAdvance() {
		return assignInterestAdvance;
	}

	public void setAssignInterestAdvance(String assignInterestAdvance) {
		this.assignInterestAdvance = assignInterestAdvance;
	}

	public String getAssignPay() {
		return assignPay;
	}

	public void setAssignPay(String assignPay) {
		this.assignPay = assignPay;
	}

	public String getServiceFeeRate() {
		return serviceFeeRate;
	}

	public void setServiceFeeRate(String serviceFeeRate) {
		this.serviceFeeRate = serviceFeeRate;
	}

	public String getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(String serviceFee) {
		this.serviceFee = serviceFee;
	}

	public String getAssignTime() {
		return assignTime;
	}

	public void setAssignTime(String assignTime) {
		this.assignTime = assignTime;
	}

	public String getBorrowPeriod() {
		return borrowPeriod;
	}

	public void setBorrowPeriod(String borrowPeriod) {
		this.borrowPeriod = borrowPeriod;
	}

	public String getAssignPeriod() {
		return assignPeriod;
	}

	public void setAssignPeriod(String assignPeriod) {
		this.assignPeriod = assignPeriod;
	}

	public String getAssignTypeName() {
		return assignTypeName;
	}

	public void setAssignTypeName(String assignTypeName) {
		this.assignTypeName = assignTypeName;
	}

}
