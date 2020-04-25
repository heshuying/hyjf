package com.hyjf.mybatis.model.customize.app;

import java.io.Serializable;

/**
 * 
 * 已承接列表---承接详情Bean
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年8月4日
 * @see 下午3:08:14
 */
public class AppTenderCreditAssignedDetailCustomize implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -8073436034212184025L;

    /**
     * 承接债权的原项目编号
     */
    private String bidNid;

    /**
     * 预期年化收益率
     */
    private String bidApr;

    /**
     * 项目期限
     */
    private String creditTerm;

    /**
     * 出借本金
     */
    private String assignCapital;

    /**
     * 折价率
     */
    private String creditDiscount;

    /**
     * 垫付利息
     */
    private String assigninterestAdvance;

    /**
     * 支付价格
     */
    private String assignPay;

    /**
     * 支付时间
     */
    private String addTime;

    /**
     * 到期预估本息(回收总额)
     */
    private String assignAccount;

    /**
     * 到期预估收益(债转利息)
     */
    private String assignInterest;

    /**
     * 最近应还时间
     */
    private String latelyRepayTime;

    /**
     * 已还总额 累计到账金额
     */
    private String assignRepayAccount;

    /**
     * 还款期数
     */
    private String assignRepayPeriod;

    /**
     * 已还款期数
     */
    private String recoverPeriod;

    /**
     * 已还款期数 = 还款期数 : 已还款(2)
     * 已还款期数 = 0 : 未还款(1)
     * 已还款期数 != 0 且 已还款期数 < 还款期数 :还款中(0)
     * 0:还款中,1:未还款,2:已还款
     */
    private String status;

    /**
     * 还款状态
     */
    private String recoverStatus;

    public String getBidNid() {
        return bidNid;
    }

    public void setBidNid(String bidNid) {
        this.bidNid = bidNid;
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

    public String getAssigninterestAdvance() {
        return assigninterestAdvance;
    }

    public void setAssigninterestAdvance(String assigninterestAdvance) {
        this.assigninterestAdvance = assigninterestAdvance;
    }

    public String getAssignPay() {
        return assignPay;
    }

    public void setAssignPay(String assignPay) {
        this.assignPay = assignPay;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getAssignAccount() {
        return assignAccount;
    }

    public void setAssignAccount(String assignAccount) {
        this.assignAccount = assignAccount;
    }

    public String getAssignInterest() {
        return assignInterest;
    }

    public void setAssignInterest(String assignInterest) {
        this.assignInterest = assignInterest;
    }

    public String getLatelyRepayTime() {
        return latelyRepayTime;
    }

    public void setLatelyRepayTime(String latelyRepayTime) {
        this.latelyRepayTime = latelyRepayTime;
    }

    public String getAssignRepayAccount() {
        return assignRepayAccount;
    }

    public void setAssignRepayAccount(String assignRepayAccount) {
        this.assignRepayAccount = assignRepayAccount;
    }

    public String getAssignRepayPeriod() {
        return assignRepayPeriod;
    }

    public void setAssignRepayPeriod(String assignRepayPeriod) {
        this.assignRepayPeriod = assignRepayPeriod;
    }

    public String getRecoverPeriod() {
        return recoverPeriod;
    }

    public void setRecoverPeriod(String recoverPeriod) {
        this.recoverPeriod = recoverPeriod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRecoverStatus() {
        return recoverStatus;
    }

    public void setRecoverStatus(String recoverStatus) {
        this.recoverStatus = recoverStatus;
    }

}
