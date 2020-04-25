
package com.hyjf.mybatis.model.customize.app;

import java.io.Serializable;

/**
 * @author xiasq
 * @version AppMyHjhDetailCustomize, v0.1 2017/12/11 19:14
 */
public class AppMyHjhDetailCustomize implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 5748630051215873837L;

	// 计划nid
	private String planNid;
	// 计划名称
	private String planName;
	// 计划出借订单号
	private String accedeOrderId;
	// 计划年化收益率
	private String planApr;
	// 计划锁定期
	private String planPeriod;
	// 计划出借用户id
	private String userId;
	// 计划出借金额
	private String accedeAccount;
	// 计划出借时间
	private String addTime;
	// 计划待收本金
	private String waitCaptical;
	// 计划待收收益
	private String waitInterest;
	// 计划回款总额
	private String waitTotal;
	// 计划已回款总额
	private String receivedTotal;
	// 还款方式 已翻译
	private String repayMethod;
	// 还款方式 代号
	private String repayStyle;
	// 订单状态
	private String orderStatus;
	// 预计回款时间
	private String lastPaymentTime;
    // add 汇计划二期前端优化 修改锁定期的显示方式  nxl 20180426 start
	// 计息时间
	private String countInterestTime;
    // add 汇计划二期前端优化 修改锁定期的显示方式  nxl 20180426 end

	public String getPlanNid() {
		return planNid;
	}

	public void setPlanNid(String planNid) {
		this.planNid = planNid;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getAccedeOrderId() {
		return accedeOrderId;
	}

	public void setAccedeOrderId(String accedeOrderId) {
		this.accedeOrderId = accedeOrderId;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccedeAccount() {
		return accedeAccount;
	}

	public void setAccedeAccount(String accedeAccount) {
		this.accedeAccount = accedeAccount;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

    public String getWaitCaptical() {
        return waitCaptical;
    }

    public void setWaitCaptical(String waitCaptical) {
        this.waitCaptical = waitCaptical;
    }

    public String getWaitInterest() {
		return waitInterest;
	}

	public void setWaitInterest(String waitInterest) {
		this.waitInterest = waitInterest;
	}

	public String getReceivedTotal() {
		return receivedTotal;
	}

	public void setReceivedTotal(String receivedTotal) {
		this.receivedTotal = receivedTotal;
	}

	public String getRepayMethod() {
		return repayMethod;
	}

	public void setRepayMethod(String repayMethod) {
		this.repayMethod = repayMethod;
	}

	public String getRepayStyle() {
		return repayStyle;
	}

	public void setRepayStyle(String repayStyle) {
		this.repayStyle = repayStyle;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getWaitTotal() {
		return waitTotal;
	}

	public void setWaitTotal(String waitTotal) {
		this.waitTotal = waitTotal;
	}

	public String getLastPaymentTime() {
		return lastPaymentTime;
	}

	public void setLastPaymentTime(String lastPaymentTime) {
		this.lastPaymentTime = lastPaymentTime;
	}

	public String getCountInterestTime() {
		return countInterestTime;
	}

	public void setCountInterestTime(String countInterestTime) {
		this.countInterestTime = countInterestTime;
	}
	
}
