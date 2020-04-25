package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountAccurate implements Serializable {
    private Integer id;

    private Integer userId;

    private BigDecimal total;

    private BigDecimal income;

    private BigDecimal expend;

    private BigDecimal balance;

    private BigDecimal balanceCash;

    private BigDecimal balanceFrost;

    private BigDecimal frost;

    private BigDecimal await;

    private BigDecimal repay;

    private BigDecimal frostCash;

    private Integer isUpdate;

    private Boolean isok;

    private BigDecimal recMoney;

    private BigDecimal fee;

    private BigDecimal inMoney;

    private Integer inMoneyFlag;

    private BigDecimal recoverInterest;

    private BigDecimal investTotal;

    private BigDecimal waitInterest;

    private BigDecimal waitCapital;

    private static final long serialVersionUID = 1L;

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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getExpend() {
        return expend;
    }

    public void setExpend(BigDecimal expend) {
        this.expend = expend;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalanceCash() {
        return balanceCash;
    }

    public void setBalanceCash(BigDecimal balanceCash) {
        this.balanceCash = balanceCash;
    }

    public BigDecimal getBalanceFrost() {
        return balanceFrost;
    }

    public void setBalanceFrost(BigDecimal balanceFrost) {
        this.balanceFrost = balanceFrost;
    }

    public BigDecimal getFrost() {
        return frost;
    }

    public void setFrost(BigDecimal frost) {
        this.frost = frost;
    }

    public BigDecimal getAwait() {
        return await;
    }

    public void setAwait(BigDecimal await) {
        this.await = await;
    }

    public BigDecimal getRepay() {
        return repay;
    }

    public void setRepay(BigDecimal repay) {
        this.repay = repay;
    }

    public BigDecimal getFrostCash() {
        return frostCash;
    }

    public void setFrostCash(BigDecimal frostCash) {
        this.frostCash = frostCash;
    }

    public Integer getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Integer isUpdate) {
        this.isUpdate = isUpdate;
    }

    public Boolean getIsok() {
        return isok;
    }

    public void setIsok(Boolean isok) {
        this.isok = isok;
    }

    public BigDecimal getRecMoney() {
        return recMoney;
    }

    public void setRecMoney(BigDecimal recMoney) {
        this.recMoney = recMoney;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getInMoney() {
        return inMoney;
    }

    public void setInMoney(BigDecimal inMoney) {
        this.inMoney = inMoney;
    }

    public Integer getInMoneyFlag() {
        return inMoneyFlag;
    }

    public void setInMoneyFlag(Integer inMoneyFlag) {
        this.inMoneyFlag = inMoneyFlag;
    }

    public BigDecimal getRecoverInterest() {
        return recoverInterest;
    }

    public void setRecoverInterest(BigDecimal recoverInterest) {
        this.recoverInterest = recoverInterest;
    }

    public BigDecimal getInvestTotal() {
        return investTotal;
    }

    public void setInvestTotal(BigDecimal investTotal) {
        this.investTotal = investTotal;
    }

    public BigDecimal getWaitInterest() {
        return waitInterest;
    }

    public void setWaitInterest(BigDecimal waitInterest) {
        this.waitInterest = waitInterest;
    }

    public BigDecimal getWaitCapital() {
        return waitCapital;
    }

    public void setWaitCapital(BigDecimal waitCapital) {
        this.waitCapital = waitCapital;
    }
}