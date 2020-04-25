package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductStatisCustomize implements Serializable {
    private Integer id;

    private Integer inCount;

    private Integer outCount;

    private BigDecimal inAmount;

    private BigDecimal outAmount;

    private BigDecimal outInterest;

    private BigDecimal loanBalance;

    private BigDecimal investAmount;

    private Integer createTime;

    private String dataDate;

    private String dataMonth;

//日期选择查询
    private Integer timeStart;
    private Integer timeEnd;
    private String vFlag;
    
    private Integer userId;//用户id
    private BigDecimal principal;//出借人本金
    /***
     * 新老用户分布
     */
    private BigDecimal amount;//操作金额
    private Integer regTime;//注册时间
    
    
    public Integer getRegTime() {
		return regTime;
	}

	public void setRegTime(Integer regTime) {
		this.regTime = regTime;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getPrincipal() {
		return principal;
	}

	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	public String getvFlag() {
		return vFlag;
	}

	public void setvFlag(String vFlag) {
		this.vFlag = vFlag;
	}

	private static final long serialVersionUID = 1L;

	public Integer getId() {
		return id;
	}

	public Integer getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Integer timeStart) {
		this.timeStart = timeStart;
	}

	public Integer getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Integer timeEnd) {
		this.timeEnd = timeEnd;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getInCount() {
		return inCount;
	}

	public void setInCount(Integer inCount) {
		this.inCount = inCount;
	}

	public Integer getOutCount() {
		return outCount;
	}

	public void setOutCount(Integer outCount) {
		this.outCount = outCount;
	}

	public BigDecimal getInAmount() {
		return inAmount;
	}

	public void setInAmount(BigDecimal inAmount) {
		this.inAmount = inAmount;
	}

	public BigDecimal getOutAmount() {
		return outAmount;
	}

	public void setOutAmount(BigDecimal outAmount) {
		this.outAmount = outAmount;
	}

	public BigDecimal getOutInterest() {
		return outInterest;
	}

	public void setOutInterest(BigDecimal outInterest) {
		this.outInterest = outInterest;
	}

	public BigDecimal getLoanBalance() {
		return loanBalance;
	}

	public void setLoanBalance(BigDecimal loanBalance) {
		this.loanBalance = loanBalance;
	}

	public BigDecimal getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public String getDataDate() {
		return dataDate;
	}

	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}

	public String getDataMonth() {
		return dataMonth;
	}

	public void setDataMonth(String dataMonth) {
		this.dataMonth = dataMonth;
	}

}