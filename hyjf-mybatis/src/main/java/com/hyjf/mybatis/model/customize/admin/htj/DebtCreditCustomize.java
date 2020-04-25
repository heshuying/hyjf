/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 *           Created at: 2015年11月20日 下午5:24:10
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.mybatis.model.customize.admin.htj;

import java.io.Serializable;

/**
 * @author Administrator
 */

public class DebtCreditCustomize implements Serializable {

    /**
     * serialVersionUID:序列化id
     */

    private static final long serialVersionUID = -7215139114727210609L;

    /**
     * 序号
     */
    private String id;

    /**
     * 出让人计划编号
     */
    private String planNid;

    /**
     * 出让人计划订单号
     */
    private String planOrderId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 债转编号
     */
    private String creditNid;

    /**
     * 项目编号
     */
    private String borrowNid;

    /**
     * 项目收益率
     */
    private String borrowApr;

    /**
     * 还款方式
     */
    private String repayStyleName;

    /**
     * 债权本金
     */
    private String creditCapital;

    /**
     * 预期实际收益率
     */
    private String actualApr;

    /**
     * 清算手续费率
     */
    private String serviceFeeRate;

    /**
     * 实际服务费
     */
    private String serviceFee;

    /**
     * 承接总额
     */
    private String assignAccount;

    /**
     * 已转让本金
     */
    private String assignCapital;

    /**
     * 已转让垫付利息
     */
    private String assignAdvanceInterest;

    /**
     * 出让人实际到账金额
     */
    private String accountReceive;

    /**
     * 实际清算时间
     */
    private String liquidatesTime;

    /**
     * 转让状态
     */
    private String creditStatusName;

    /**
     * 还款状态
     */
    private String repayStatusName;
    /**
     * 项目总期数
     */
    private String borrowPeriod;

    /**
     * 承接原项目所在期数
     */
    private String assignPeriod;

    /**
     * 清算时所在期数
     */
    private String liquidatesPeriod;

    /**
     * 还款时间
     */
    private String repayNextTime;

    /**
     * 预期收益
     */
    private String repayInterest;

    /**
     * 清算时公允价值
     */
    private String liquidationFairValue;

    /**
     * 计划余额
     */
    private String accedeBalance;

    /**
     * 计划冻结金额
     */
    private String accedeFrost;

    /**
     * 计划加入金额
     */
    private String accedeAccount;

    /**
     * 清算前已还款金额
     */
    private String liquidatesRepayFrost;

    /**
     * 清算时公允价值总和
     */
    private String liquidationFairValueSum;

    /**
     * 清算时计划订单余额
     */
    private String accedeBalanceSum;

    /**
     * 清算时冻结接总和
     */
    private String accedeFrostSum;

    /**
     * 项目还款金额总和
     */
    private String liquidatesRepayFrostSum;

    /**
     * 转让到账金额总和
     */
    private String accountReceiveSum;
    /**
     * 清算进度
     */
    private String liquidatesApr;

    private int limitStart = -1;

    private int limitEnd = -1;

    /**
     * 构造方法
     */
    public DebtCreditCustomize() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanNid() {
        return planNid;
    }

    public void setPlanNid(String planNid) {
        this.planNid = planNid;
    }

    public String getPlanOrderId() {
        return planOrderId;
    }

    public void setPlanOrderId(String planOrderId) {
        this.planOrderId = planOrderId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getBorrowApr() {
        return borrowApr;
    }

    public void setBorrowApr(String borrowApr) {
        this.borrowApr = borrowApr;
    }

    public String getRepayStyleName() {
        return repayStyleName;
    }

    public void setRepayStyleName(String repayStyleName) {
        this.repayStyleName = repayStyleName;
    }

    public String getCreditCapital() {
        return creditCapital;
    }

    public void setCreditCapital(String creditCapital) {
        this.creditCapital = creditCapital;
    }

    public String getActualApr() {
        return actualApr;
    }

    public void setActualApr(String actualApr) {
        this.actualApr = actualApr;
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

    public String getAssignCapital() {
        return assignCapital;
    }

    public void setAssignCapital(String assignCapital) {
        this.assignCapital = assignCapital;
    }

    public String getAssignAdvanceInterest() {
        return assignAdvanceInterest;
    }

    public void setAssignAdvanceInterest(String assignAdvanceInterest) {
        this.assignAdvanceInterest = assignAdvanceInterest;
    }

    public String getAssignAccount() {
        return assignAccount;
    }

    public void setAssignAccount(String assignAccount) {
        this.assignAccount = assignAccount;
    }

    public String getAccountReceive() {
        return accountReceive;
    }

    public void setAccountReceive(String accountReceive) {
        this.accountReceive = accountReceive;
    }

    public String getLiquidatesTime() {
        return liquidatesTime;
    }

    public void setLiquidatesTime(String liquidatesTime) {
        this.liquidatesTime = liquidatesTime;
    }

    public String getCreditStatusName() {
        return creditStatusName;
    }

    public void setCreditStatusName(String creditStatusName) {
        this.creditStatusName = creditStatusName;
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

    public String getLiquidatesPeriod() {
        return liquidatesPeriod;
    }

    public void setLiquidatesPeriod(String liquidatesPeriod) {
        this.liquidatesPeriod = liquidatesPeriod;
    }

    public String getRepayNextTime() {
        return repayNextTime;
    }

    public void setRepayNextTime(String repayNextTime) {
        this.repayNextTime = repayNextTime;
    }

    public String getRepayInterest() {
        return repayInterest;
    }

    public void setRepayInterest(String repayInterest) {
        this.repayInterest = repayInterest;
    }

    public String getLiquidationFairValue() {
        return liquidationFairValue;
    }

    public void setLiquidationFairValue(String liquidationFairValue) {
        this.liquidationFairValue = liquidationFairValue;
    }

    public String getAccedeBalance() {
        return accedeBalance;
    }

    public void setAccedeBalance(String accedeBalance) {
        this.accedeBalance = accedeBalance;
    }

    public String getAccedeFrost() {
        return accedeFrost;
    }

    public void setAccedeFrost(String accedeFrost) {
        this.accedeFrost = accedeFrost;
    }

    public String getAccedeAccount() {
        return accedeAccount;
    }

    public void setAccedeAccount(String accedeAccount) {
        this.accedeAccount = accedeAccount;
    }

    public String getLiquidatesRepayFrost() {
        return liquidatesRepayFrost;
    }

    public void setLiquidatesRepayFrost(String liquidatesRepayFrost) {
        this.liquidatesRepayFrost = liquidatesRepayFrost;
    }

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

    public String getLiquidationFairValueSum() {
        return liquidationFairValueSum;
    }

    public void setLiquidationFairValueSum(String liquidationFairValueSum) {
        this.liquidationFairValueSum = liquidationFairValueSum;
    }

    public String getAccedeBalanceSum() {
        return accedeBalanceSum;
    }

    public void setAccedeBalanceSum(String accedeBalanceSum) {
        this.accedeBalanceSum = accedeBalanceSum;
    }

    public String getAccedeFrostSum() {
        return accedeFrostSum;
    }

    public void setAccedeFrostSum(String accedeFrostSum) {
        this.accedeFrostSum = accedeFrostSum;
    }

    public String getLiquidatesRepayFrostSum() {
        return liquidatesRepayFrostSum;
    }

    public void setLiquidatesRepayFrostSum(String liquidatesRepayFrostSum) {
        this.liquidatesRepayFrostSum = liquidatesRepayFrostSum;
    }

    public String getAccountReceiveSum() {
        return accountReceiveSum;
    }

    public void setAccountReceiveSum(String accountReceiveSum) {
        this.accountReceiveSum = accountReceiveSum;
    }

	public String getLiquidatesApr() {
		return liquidatesApr;
	}

	public void setLiquidatesApr(String liquidatesApr) {
		this.liquidatesApr = liquidatesApr;
	}

	public String getRepayStatusName() {
		return repayStatusName;
	}

	public void setRepayStatusName(String repayStatusName) {
		this.repayStatusName = repayStatusName;
	}

}
