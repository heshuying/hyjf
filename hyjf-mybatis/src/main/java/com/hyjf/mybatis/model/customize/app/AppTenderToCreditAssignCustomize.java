/**
 * Description:App用户出借债转认购债券实体类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月20日 下午5:24:10
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize.app;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */

public class AppTenderToCreditAssignCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * 借款编号
	 */
	private String borrowNid;
	/**
     * 债转编号
     */
    private String creditNid;
    /**
     * 出借编号
     */
    private String tenderNid;
    /**
     * 转让本金
     */
    private String creditCapital;
    
    /**
     * 转让本金(计算用)
     */
    private BigDecimal creditCapitalNum;
    /**
     * 可承接本金
     */
    private String assignCapital;
    
    /**
     * 可承接本金(计算用)
     */
    private BigDecimal assignCapitalNum;
    
	/**
	 * 折价率
	 */
	private String creditDiscount;
	/**
     * 折后价格
     */
	private String assignPrice;
	
	/*
	 * 折后价格(计算用)
	 */
	private BigDecimal assignPriceNum;
	/**
     * 实际支付
     */
    private String assignPay;
    
    /**
     * 实际支付(计算用)
     */
    private BigDecimal assignPayNum;
    
	/**
     * 预期收益
     */
    private String assignInterest;
    
    /**
     * 预期收益(计算用)
     */
    private BigDecimal assignInterestNum;
	/**
     * 垫付利息
     */
	private String assignInterestAdvance;
	
	/**
	 * 垫付利息(计算用)
	 */
	private BigDecimal assignInterestAdvanceNum;
	/**
     * 实际支付文本
     */
    private String assignPayText;
    /**
     * 债转期全部利息
     */
    private String creditInterest;
    
    /**
     * 债转期全部利息(计算用)
     */
    private BigDecimal creditInterestNum;
    /**
     * 债转利息
     */
    private String assignPayInterest;
	
    /**
     * 债转利息(计算用)
     */
    private BigDecimal assignPayInterestNum;
    
	
    public String getBorrowNid() {
        return borrowNid;
    }
    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }
    public String getCreditNid() {
        return creditNid;
    }
    public void setCreditNid(String creditNid) {
        this.creditNid = creditNid;
    }
    public String getTenderNid() {
        return tenderNid;
    }
    public void setTenderNid(String tenderNid) {
        this.tenderNid = tenderNid;
    }
    public String getCreditCapital() {
        return creditCapital;
    }
    public void setCreditCapital(String creditCapital) {
        this.creditCapital = creditCapital;
    }
    public String getAssignCapital() {
        return assignCapital;
    }
    public void setAssignCapital(String assignCapital) {
        this.assignCapital = assignCapital;
    }
    public String getCreditDiscount() {
        return creditDiscount;
    }
    public void setCreditDiscount(String creditDiscount) {
        this.creditDiscount = creditDiscount;
    }
    public String getAssignPrice() {
        return assignPrice;
    }
    public void setAssignPrice(String assignPrice) {
        this.assignPrice = assignPrice;
    }
    public String getAssignInterestAdvance() {
        return assignInterestAdvance;
    }
    public void setAssignInterestAdvance(String assignInterestAdvance) {
        this.assignInterestAdvance = assignInterestAdvance;
    }
    public String getAssignInterest() {
        return assignInterest;
    }
    public void setAssignInterest(String assignInterest) {
        this.assignInterest = assignInterest;
    }
    public String getAssignPayText() {
        return assignPayText;
    }
    public void setAssignPayText(String assignPayText) {
        this.assignPayText = assignPayText;
    }
    public String getAssignPay() {
        return assignPay;
    }
    public void setAssignPay(String assignPay) {
        this.assignPay = assignPay;
    }
    public String getCreditInterest() {
        return creditInterest;
    }
    public void setCreditInterest(String creditInterest) {
        this.creditInterest = creditInterest;
    }
    public String getAssignPayInterest() {
        return assignPayInterest;
    }
    public void setAssignPayInterest(String assignPayInterest) {
        this.assignPayInterest = assignPayInterest;
    }
    public BigDecimal getAssignInterestAdvanceNum() {
        return assignInterestAdvanceNum;
    }
    public void setAssignInterestAdvanceNum(BigDecimal assignInterestAdvanceNum) {
        this.assignInterestAdvanceNum = assignInterestAdvanceNum;
    }
    public BigDecimal getCreditCapitalNum() {
        return creditCapitalNum;
    }
    public void setCreditCapitalNum(BigDecimal creditCapitalNum) {
        this.creditCapitalNum = creditCapitalNum;
    }
    public BigDecimal getCreditInterestNum() {
        return creditInterestNum;
    }
    public void setCreditInterestNum(BigDecimal creditInterestNum) {
        this.creditInterestNum = creditInterestNum;
    }
    public BigDecimal getAssignPayInterestNum() {
        return assignPayInterestNum;
    }
    public void setAssignPayInterestNum(BigDecimal assignPayInterestNum) {
        this.assignPayInterestNum = assignPayInterestNum;
    }
    public BigDecimal getAssignInterestNum() {
        return assignInterestNum;
    }
    public void setAssignInterestNum(BigDecimal assignInterestNum) {
        this.assignInterestNum = assignInterestNum;
    }
    public BigDecimal getAssignCapitalNum() {
        return assignCapitalNum;
    }
    public void setAssignCapitalNum(BigDecimal assignCapitalNum) {
        this.assignCapitalNum = assignCapitalNum;
    }
    public BigDecimal getAssignPayNum() {
        return assignPayNum;
    }
    public void setAssignPayNum(BigDecimal assignPayNum) {
        this.assignPayNum = assignPayNum;
    }
    public BigDecimal getAssignPriceNum() {
        return assignPriceNum;
    }
    public void setAssignPriceNum(BigDecimal assignPriceNum) {
        this.assignPriceNum = assignPriceNum;
    }
    
}
