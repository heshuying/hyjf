package com.hyjf.mybatis.model.customize.app;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * 债权转让--已承接债转列表listBean
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年8月2日
 * @see 上午11:43:14
 */
public class AppTenderCreditAssignedListCustomize implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -3579795481066426562L;

    /**
     * 项目编号
     */
    private String bidNid;

    /**
     * 债转编号
     */
    private String creditNid;

    /**
     * 出借本金
     */
    private String assignCapital;

    /**
     * 回收总额
     */
    private BigDecimal assignAccount;

    /**
     * 已还总额
     */
    private BigDecimal assignRepayAccount;

    /**
     * 待收金额=回收总额-已还总额
     */
    private String awaitMoney;

    /**
     * 下次还款时间
     */
    private String assignRepayNextTime;

    /**
     * 还款状态
     */
    private String status;

    /**
     * 承接详情url
     */
    private String creditUrl;

    /**
     * 债转协议url
     */
    private String creditContractUrl;
    
    /**
     * 项目id
     */
    private String borrowNid;

    /**
     * orderId
     */
    private String orderId;

    /**
     * isCouponTender
     */
    private String isCouponTender;

    public String getCreditNid() {
        return creditNid;
    }

    public void setCreditNid(String creditNid) {
        this.creditNid = creditNid;
    }

    public String getAssignCapital() {
        return assignCapital;
    }

    public void setAssignCapital(String assignCapital) {
        this.assignCapital = assignCapital;
    }

    public BigDecimal getAssignAccount() {
        return assignAccount;
    }

    public void setAssignAccount(BigDecimal assignAccount) {
        this.assignAccount = assignAccount;
    }

    public BigDecimal getAssignRepayAccount() {
        return assignRepayAccount;
    }

    public void setAssignRepayAccount(BigDecimal assignRepayAccount) {
        this.assignRepayAccount = assignRepayAccount;
    }

    public String getAwaitMoney() {
        return awaitMoney;
    }

    public void setAwaitMoney(String awaitMoney) {
        this.awaitMoney = awaitMoney;
    }

    public String getAssignRepayNextTime() {
        return assignRepayNextTime;
    }

    public void setAssignRepayNextTime(String assignRepayNextTime) {
        this.assignRepayNextTime = assignRepayNextTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBidNid() {
        return bidNid;
    }

    public void setBidNid(String bidNid) {
        this.bidNid = bidNid;
    }

    public String getCreditUrl() {
        return creditUrl;
    }

    public void setCreditUrl(String creditUrl) {
        this.creditUrl = creditUrl;
    }

    public String getCreditContractUrl() {
        return creditContractUrl;
    }

    public void setCreditContractUrl(String creditContractUrl) {
        this.creditContractUrl = creditContractUrl;
    }

    public String getBorrowNid() {
        return borrowNid;
    }

    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getIsCouponTender() {
        return isCouponTender;
    }

    public void setIsCouponTender(String isCouponTender) {
        this.isCouponTender = isCouponTender;
    }

}
