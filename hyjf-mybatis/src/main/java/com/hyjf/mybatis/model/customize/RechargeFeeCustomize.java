package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 充值手续费对账
 * @author Michael
 */

public class RechargeFeeCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;
	/**
	 * 用户id
	 */
	private Integer userId;
	/**
	 * 用户名
	 */
	private String userName;
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
	 * 垫付手续费
	 */
	private BigDecimal rechargeFee;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
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
	public BigDecimal getRechargeFee() {
		return rechargeFee;
	}
	public void setRechargeFee(BigDecimal rechargeFee) {
		this.rechargeFee = rechargeFee;
	}
	
}
