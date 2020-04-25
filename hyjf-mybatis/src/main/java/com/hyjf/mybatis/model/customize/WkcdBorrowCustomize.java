package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

public class WkcdBorrowCustomize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String bid;
	private String wkcd_id;
	private String recoverPeriod; // 还款期数
	private String recoverCapital; // 应还本金
	private String recoverInterest; // 利息
	private String recoverFee;// 管理费
	private String aheadDays;// 提前还款天数
	private String chargeInterest; // 提前减息
	private String lateDays; // 逾期天数
	private String lateInterest; // 逾期罚息
	private String recoverTime; //还款日期
	private Integer recoverLastTime; //最后一笔放款时间

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getWkcd_id() {
		return wkcd_id;
	}

	public void setWkcd_id(String wkcd_id) {
		this.wkcd_id = wkcd_id;
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

	public String getRecoverFee() {
		return recoverFee;
	}

	public void setRecoverFee(String recoverFee) {
		this.recoverFee = recoverFee;
	}

	public String getAheadDays() {
		return aheadDays;
	}

	public void setAheadDays(String aheadDays) {
		this.aheadDays = aheadDays;
	}

	public String getChargeInterest() {
		return chargeInterest;
	}

	public void setChargeInterest(String chargeInterest) {
		this.chargeInterest = chargeInterest;
	}

	public String getLateDays() {
		return lateDays;
	}

	public void setLateDays(String lateDays) {
		this.lateDays = lateDays;
	}

	public String getLateInterest() {
		return lateInterest;
	}

	public void setLateInterest(String lateInterest) {
		this.lateInterest = lateInterest;
	}

	public String getRecoverTime() {
		return recoverTime;
	}

	public void setRecoverTime(String recoverTime) {
		this.recoverTime = recoverTime;
	}

	public Integer getRecoverLastTime() {
		return recoverLastTime;
	}

	public void setRecoverLastTime(Integer recoverLastTime) {
		this.recoverLastTime = recoverLastTime;
	}
	
	

}
