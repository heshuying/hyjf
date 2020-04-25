package com.hyjf.mybatis.model.customize;

import java.math.BigDecimal;

public class ProductInterestCustomize  {
	private Integer id;//主键
    private Integer userId;//用户id
    private String orderId;//订单id
    private BigDecimal amount;//计息金额
    private Integer interestDays;//计息天数
    private BigDecimal interestRate;//日利率
    private BigDecimal interest;//总利息
    private String interestTime;//时间
    private String username;//用户名
    private String refername;//推荐人
    
    /**
     * 分页信息添加
     */
    protected int limitStart = -1;
    protected int limitEnd = -1;

	/**
	 * 检索条件 时间开始
	 */
	private String timeStartSrch;

	/**
	 * 检索条件 时间结束
	 */
	private String timeEndSrch;

	public String getTimeStartSrch() {
		return timeStartSrch;
	}
	public void setTimeStartSrch(String timeStartSrch) {
		this.timeStartSrch = timeStartSrch;
	}
	public String getTimeEndSrch() {
		return timeEndSrch;
	}
	public void setTimeEndSrch(String timeEndSrch) {
		this.timeEndSrch = timeEndSrch;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getInterestDays() {
		return interestDays;
	}
	public void setInterestDays(Integer interestDays) {
		this.interestDays = interestDays;
	}
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public String getInterestTime() {
		return interestTime;
	}
	public void setInterestTime(String interestTime) {
		this.interestTime = interestTime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRefername() {
		return refername;
	}
	public void setRefername(String refername) {
		this.refername = refername;
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
    
   
    
    
}