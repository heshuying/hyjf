package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *	充值手续费统计
 * @author Michael
 */

public class RechargeFeeStatisticsCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;
	/**
	 * 统计开始时间
	 */
	private int startTime;
	/**
	 * 统计结束时间
	 */
	private int endTime;
	/**
	 * 累计充值金额
	 */
	private BigDecimal rechargeAmount;
	
	/**
	 * 个人网银累计充值金额
	 */
	private BigDecimal personalAmount;

	/**
	 * 快捷充值累计充值金额
	 */
	private BigDecimal quickAmount;

	/**
	 * 企业网银累计充值金额
	 */
	private BigDecimal compAmount;

	/**
	 * 平台垫付金额
	 */
	private BigDecimal dianfuAmount;
	
	/**
	 * 充值总金额
	 */
	private BigDecimal rechargeAmountSum;
	/**
	 * 个人网银充值总金额
	 */
	private BigDecimal personalAmountSum;
	/**
	 * 快捷充值总金额
	 */
	private BigDecimal quickAmountSum;
	/**
	 * 企业网银充值总金额
	 */
	private BigDecimal compAmountSum;
	/**
	 * 垫付总金额
	 */
	private BigDecimal feeSum;
	/**
	 * 开始时间
	 */
	private String startDate;
	/**
	 *结束时间
	 */
	private String endDate;
	
	

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public BigDecimal getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(BigDecimal rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public BigDecimal getPersonalAmount() {
		return personalAmount;
	}

	public void setPersonalAmount(BigDecimal personalAmount) {
		this.personalAmount = personalAmount;
	}

	public BigDecimal getQuickAmount() {
		return quickAmount;
	}

	public void setQuickAmount(BigDecimal quickAmount) {
		this.quickAmount = quickAmount;
	}

	public BigDecimal getCompAmount() {
		return compAmount;
	}

	public void setCompAmount(BigDecimal compAmount) {
		this.compAmount = compAmount;
	}

	public BigDecimal getDianfuAmount() {
		return dianfuAmount;
	}

	public void setDianfuAmount(BigDecimal dianfuAmount) {
		this.dianfuAmount = dianfuAmount;
	}

	public BigDecimal getRechargeAmountSum() {
		return rechargeAmountSum;
	}

	public void setRechargeAmountSum(BigDecimal rechargeAmountSum) {
		this.rechargeAmountSum = rechargeAmountSum;
	}

	public BigDecimal getPersonalAmountSum() {
		return personalAmountSum;
	}

	public void setPersonalAmountSum(BigDecimal personalAmountSum) {
		this.personalAmountSum = personalAmountSum;
	}

	public BigDecimal getQuickAmountSum() {
		return quickAmountSum;
	}

	public void setQuickAmountSum(BigDecimal quickAmountSum) {
		this.quickAmountSum = quickAmountSum;
	}

	public BigDecimal getCompAmountSum() {
		return compAmountSum;
	}

	public void setCompAmountSum(BigDecimal compAmountSum) {
		this.compAmountSum = compAmountSum;
	}

	public BigDecimal getFeeSum() {
		return feeSum;
	}

	public void setFeeSum(BigDecimal feeSum) {
		this.feeSum = feeSum;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	
}
