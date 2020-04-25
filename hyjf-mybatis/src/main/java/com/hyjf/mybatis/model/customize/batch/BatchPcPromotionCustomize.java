package com.hyjf.mybatis.model.customize.batch;

import java.io.Serializable;
import java.math.BigDecimal;

public class BatchPcPromotionCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7422303407013732935L;

	/**
	 * 用户Id
	 */
	private Integer userId;

	/**
	 * 首投金额
	 */
	private BigDecimal investAmount;

	/**
	 * 首投项目类型
	 */
	private String investProjectType;

	/**
	 * 首投项目期限
	 */
	private String investProjectPeriod;

	/**
	 * 首投时间
	 */
	private String investTime;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}

	public String getInvestProjectType() {
		return investProjectType;
	}

	public void setInvestProjectType(String investProjectType) {
		this.investProjectType = investProjectType;
	}

	public String getInvestProjectPeriod() {
		return investProjectPeriod;
	}

	public void setInvestProjectPeriod(String investProjectPeriod) {
		this.investProjectPeriod = investProjectPeriod;
	}

	public String getInvestTime() {
		return investTime;
	}

	public void setInvestTime(String investTime) {
		this.investTime = investTime;
	}

}
