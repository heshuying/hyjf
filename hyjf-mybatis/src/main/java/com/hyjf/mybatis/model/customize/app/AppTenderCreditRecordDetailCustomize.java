package com.hyjf.mybatis.model.customize.app;

import java.io.Serializable;

/**
 * 
 * 转让记录详情Bean
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年8月4日
 * @see 上午10:15:16
 */
public class AppTenderCreditRecordDetailCustomize implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -9060159143907941104L;

    /**
     * 债转编号
     */
    private String creditNid;

    /**
     * 原标标号
     */
    private String bidNid;

    /**
     * 债转本金
     */
    private String creditCapital;

    /**
     * 原标年化利率
     */
    private String bidApr;

    /**
     * 债转期限
     */
    private String creditTerm;

    /**
     * 折价率
     */
    private String creditDiscount;

    /**
     * 出让价格
     */
    private String creditPrice;

    /**
     * 已认购 本金
     */
    private String creditCapitalAssigned;

    /**
     * 服务费
     */
    private String creditFee;

    /**
     * 承接人承接债转对应的服务费
     */
    private String assignCreditFee;

    /**
     * 实际到账金额 : (已认购本金 × (1-折价率)+ 垫付利息)-服务费用
     */
    private String assignPay;

    /**
     * 垫付利息
     */
    private String creditInterestAssigned;

    /**
     * 转让明细URL
     */
    private String transferDetailUrl;

    /**
     * 转让进度
     */
    private String borrowSchedule;

    public String getCreditNid() {
        return creditNid;
    }

    public void setCreditNid(String creditNid) {
        this.creditNid = creditNid;
    }

    public String getCreditCapital() {
        return creditCapital;
    }

    public void setCreditCapital(String creditCapital) {
        this.creditCapital = creditCapital;
    }

    public String getBidApr() {
        return bidApr;
    }

    public void setBidApr(String bidApr) {
        this.bidApr = bidApr;
    }

    public String getCreditTerm() {
        return creditTerm;
    }

    public void setCreditTerm(String creditTerm) {
        this.creditTerm = creditTerm;
    }

    public String getCreditDiscount() {
        return creditDiscount;
    }

    public void setCreditDiscount(String creditDiscount) {
        this.creditDiscount = creditDiscount;
    }

    public String getCreditPrice() {
        return creditPrice;
    }

    public void setCreditPrice(String creditPrice) {
        this.creditPrice = creditPrice;
    }

    public String getCreditCapitalAssigned() {
        return creditCapitalAssigned;
    }

    public void setCreditCapitalAssigned(String creditCapitalAssigned) {
        this.creditCapitalAssigned = creditCapitalAssigned;
    }

    public String getCreditFee() {
        return creditFee;
    }

    public void setCreditFee(String creditFee) {
        this.creditFee = creditFee;
    }

    public String getAssignPay() {
        return assignPay;
    }

    public void setAssignPay(String assignPay) {
        this.assignPay = assignPay;
    }

    public String getCreditInterestAssigned() {
        return creditInterestAssigned;
    }

    public void setCreditInterestAssigned(String creditInterestAssigned) {
        this.creditInterestAssigned = creditInterestAssigned;
    }

    public String getTransferDetailUrl() {
        return transferDetailUrl;
    }

    public void setTransferDetailUrl(String transferDetailUrl) {
        this.transferDetailUrl = transferDetailUrl;
    }

    public String getBidNid() {
        return bidNid;
    }

    public void setBidNid(String bidNid) {
        this.bidNid = bidNid;
    }

    public String getBorrowSchedule() {
        return borrowSchedule;
    }

    public void setBorrowSchedule(String borrowSchedule) {
        this.borrowSchedule = borrowSchedule;
    }

    public String getAssignCreditFee() {
        return assignCreditFee;
    }

    public void setAssignCreditFee(String assignCreditFee) {
        this.assignCreditFee = assignCreditFee;
    }

}
