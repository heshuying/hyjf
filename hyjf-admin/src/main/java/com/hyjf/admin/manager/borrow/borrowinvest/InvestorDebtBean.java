package com.hyjf.admin.manager.borrow.borrowinvest;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hyjf.common.paginator.Paginator;

/**
 * @package com.hyjf.admin.manager.borrow.borrowinvest;
 * 传给bankcallbean的查询bean
 * @author LIBIN
 * @date 2017/07/28 
 * @version V1.0  
 */
public class InvestorDebtBean implements Serializable {

	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = -2662718983156253995L;
	
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;
	
	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;
	
	/**
	 * 开始时间
	 */
	private String startTime;
	/**
	 * 结束时间
	 */
	private String endTime;
	/** 
	 * 电子账号
	 */
	private String accountid;
	/** 
	 * 标的号 
	 */
	private String borrownid;
	/** 
	 * 用户ID 
	 */
	private String userid;
	/** 
	 * NID 
	 */
	private String nid;
	/** 
	 * 投标日期 
	 */
	private String buyDate;
	/** 
	 * 订单号 
	 */
	private String orderId;
	/** 
	 * 交易金额 
	 */
	private BigDecimal txAmount;
	/** 
	 * 预期出借利率
	 */
	private BigDecimal yield;
	/** 
	 * 预期收益
	 */
	private BigDecimal forIncome;
	/** 
	 * 预期本息收益
	 */
	private BigDecimal intTotal;
	/** 
	 * 实际收益
	 */
	private BigDecimal income;
	/** 
	 * 实际收益符号 
	 */
	private String incFlag;
	/** 
	 * 到期日
	 */
	private String endDate;
	/** 
	 * 到期日
	 */
	private String state;
	/*----add by LSY START---------*/
	private BigDecimal sumTxAmount;
	private BigDecimal sumForIncome;
	private BigDecimal sumIntTotal;
	private BigDecimal sumIncome;
	/*----add by LSY END---------*/
	public int getPaginatorPage() {
		return paginatorPage;
	}
	public void setPaginatorPage(int paginatorPage) {
		this.paginatorPage = paginatorPage;
	}
	public Paginator getPaginator() {
		return paginator;
	}
	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public String getAccountid() {
		return accountid;
	}
	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}
	public String getBorrownid() {
		return borrownid;
	}
	public void setBorrownid(String borrownid) {
		this.borrownid = borrownid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public BigDecimal getTxAmount() {
		return txAmount;
	}
	public void setTxAmount(BigDecimal txAmount) {
		this.txAmount = txAmount;
	}
	public BigDecimal getYield() {
		return yield;
	}
	public void setYield(BigDecimal yield) {
		this.yield = yield;
	}
	public BigDecimal getForIncome() {
		return forIncome;
	}
	public void setForIncome(BigDecimal forIncome) {
		this.forIncome = forIncome;
	}
	public BigDecimal getIntTotal() {
		return intTotal;
	}
	public void setIntTotal(BigDecimal intTotal) {
		this.intTotal = intTotal;
	}
	public BigDecimal getIncome() {
		return income;
	}
	public void setIncome(BigDecimal income) {
		this.income = income;
	}
	public String getIncFlag() {
		return incFlag;
	}
	public void setIncFlag(String incFlag) {
		this.incFlag = incFlag;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * sumTxAmount
	 * @return the sumTxAmount
	 */
		
	public BigDecimal getSumTxAmount() {
		return sumTxAmount;
			
	}
	/**
	 * @param sumTxAmount the sumTxAmount to set
	 */
		
	public void setSumTxAmount(BigDecimal sumTxAmount) {
		this.sumTxAmount = sumTxAmount;
			
	}
	/**
	 * sumForIncome
	 * @return the sumForIncome
	 */
		
	public BigDecimal getSumForIncome() {
		return sumForIncome;
			
	}
	/**
	 * @param sumForIncome the sumForIncome to set
	 */
		
	public void setSumForIncome(BigDecimal sumForIncome) {
		this.sumForIncome = sumForIncome;
			
	}
	/**
	 * sumIntTotal
	 * @return the sumIntTotal
	 */
		
	public BigDecimal getSumIntTotal() {
		return sumIntTotal;
			
	}
	/**
	 * @param sumIntTotal the sumIntTotal to set
	 */
		
	public void setSumIntTotal(BigDecimal sumIntTotal) {
		this.sumIntTotal = sumIntTotal;
			
	}
	/**
	 * sumIncome
	 * @return the sumIncome
	 */
		
	public BigDecimal getSumIncome() {
		return sumIncome;
			
	}
	/**
	 * @param sumIncome the sumIncome to set
	 */
		
	public void setSumIncome(BigDecimal sumIncome) {
		this.sumIncome = sumIncome;
			
	}
	
}
