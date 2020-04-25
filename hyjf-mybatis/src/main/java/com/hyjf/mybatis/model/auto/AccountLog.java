package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountLog implements Serializable {
    private Integer id;

    private String nid;

    private Integer userId;

    private String type;

    private BigDecimal total;

    private BigDecimal totalOld;

    private String code;

    private String codeType;

    private String codeNid;

    private String borrowNid;

    private BigDecimal money;

    private BigDecimal income;

    private BigDecimal incomeOld;

    private BigDecimal incomeNew;

    private Integer accountWebStatus;

    private Integer accountUserStatus;

    private String accountType;

    private BigDecimal expend;

    private BigDecimal expendOld;

    private BigDecimal expendNew;

    private BigDecimal balance;

    private BigDecimal balanceOld;

    private BigDecimal balanceNew;

    private BigDecimal balanceCash;

    private BigDecimal balanceCashOld;

    private BigDecimal balanceCashNew;

    private BigDecimal balanceFrost;

    private BigDecimal balanceFrostOld;

    private BigDecimal balanceFrostNew;

    private BigDecimal frost;

    private BigDecimal frostOld;

    private BigDecimal frostNew;

    private BigDecimal repay;

    private BigDecimal repayNew;

    private BigDecimal repayOld;

    private BigDecimal await;

    private BigDecimal awaitOld;

    private BigDecimal awaitNew;

    private Integer toUserid;

    private String remark;

    private String addtime;

    private String addip;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid == null ? null : nid.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotalOld() {
        return totalOld;
    }

    public void setTotalOld(BigDecimal totalOld) {
        this.totalOld = totalOld;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType == null ? null : codeType.trim();
    }

    public String getCodeNid() {
        return codeNid;
    }

    public void setCodeNid(String codeNid) {
        this.codeNid = codeNid == null ? null : codeNid.trim();
    }

    public String getBorrowNid() {
        return borrowNid;
    }

    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid == null ? null : borrowNid.trim();
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getIncomeOld() {
        return incomeOld;
    }

    public void setIncomeOld(BigDecimal incomeOld) {
        this.incomeOld = incomeOld;
    }

    public BigDecimal getIncomeNew() {
        return incomeNew;
    }

    public void setIncomeNew(BigDecimal incomeNew) {
        this.incomeNew = incomeNew;
    }

    public Integer getAccountWebStatus() {
        return accountWebStatus;
    }

    public void setAccountWebStatus(Integer accountWebStatus) {
        this.accountWebStatus = accountWebStatus;
    }

    public Integer getAccountUserStatus() {
        return accountUserStatus;
    }

    public void setAccountUserStatus(Integer accountUserStatus) {
        this.accountUserStatus = accountUserStatus;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType == null ? null : accountType.trim();
    }

    public BigDecimal getExpend() {
        return expend;
    }

    public void setExpend(BigDecimal expend) {
        this.expend = expend;
    }

    public BigDecimal getExpendOld() {
        return expendOld;
    }

    public void setExpendOld(BigDecimal expendOld) {
        this.expendOld = expendOld;
    }

    public BigDecimal getExpendNew() {
        return expendNew;
    }

    public void setExpendNew(BigDecimal expendNew) {
        this.expendNew = expendNew;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalanceOld() {
        return balanceOld;
    }

    public void setBalanceOld(BigDecimal balanceOld) {
        this.balanceOld = balanceOld;
    }

    public BigDecimal getBalanceNew() {
        return balanceNew;
    }

    public void setBalanceNew(BigDecimal balanceNew) {
        this.balanceNew = balanceNew;
    }

    public BigDecimal getBalanceCash() {
        return balanceCash;
    }

    public void setBalanceCash(BigDecimal balanceCash) {
        this.balanceCash = balanceCash;
    }

    public BigDecimal getBalanceCashOld() {
        return balanceCashOld;
    }

    public void setBalanceCashOld(BigDecimal balanceCashOld) {
        this.balanceCashOld = balanceCashOld;
    }

    public BigDecimal getBalanceCashNew() {
        return balanceCashNew;
    }

    public void setBalanceCashNew(BigDecimal balanceCashNew) {
        this.balanceCashNew = balanceCashNew;
    }

    public BigDecimal getBalanceFrost() {
        return balanceFrost;
    }

    public void setBalanceFrost(BigDecimal balanceFrost) {
        this.balanceFrost = balanceFrost;
    }

    public BigDecimal getBalanceFrostOld() {
        return balanceFrostOld;
    }

    public void setBalanceFrostOld(BigDecimal balanceFrostOld) {
        this.balanceFrostOld = balanceFrostOld;
    }

    public BigDecimal getBalanceFrostNew() {
        return balanceFrostNew;
    }

    public void setBalanceFrostNew(BigDecimal balanceFrostNew) {
        this.balanceFrostNew = balanceFrostNew;
    }

    public BigDecimal getFrost() {
        return frost;
    }

    public void setFrost(BigDecimal frost) {
        this.frost = frost;
    }

    public BigDecimal getFrostOld() {
        return frostOld;
    }

    public void setFrostOld(BigDecimal frostOld) {
        this.frostOld = frostOld;
    }

    public BigDecimal getFrostNew() {
        return frostNew;
    }

    public void setFrostNew(BigDecimal frostNew) {
        this.frostNew = frostNew;
    }

    public BigDecimal getRepay() {
        return repay;
    }

    public void setRepay(BigDecimal repay) {
        this.repay = repay;
    }

    public BigDecimal getRepayNew() {
        return repayNew;
    }

    public void setRepayNew(BigDecimal repayNew) {
        this.repayNew = repayNew;
    }

    public BigDecimal getRepayOld() {
        return repayOld;
    }

    public void setRepayOld(BigDecimal repayOld) {
        this.repayOld = repayOld;
    }

    public BigDecimal getAwait() {
        return await;
    }

    public void setAwait(BigDecimal await) {
        this.await = await;
    }

    public BigDecimal getAwaitOld() {
        return awaitOld;
    }

    public void setAwaitOld(BigDecimal awaitOld) {
        this.awaitOld = awaitOld;
    }

    public BigDecimal getAwaitNew() {
        return awaitNew;
    }

    public void setAwaitNew(BigDecimal awaitNew) {
        this.awaitNew = awaitNew;
    }

    public Integer getToUserid() {
        return toUserid;
    }

    public void setToUserid(Integer toUserid) {
        this.toUserid = toUserid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime == null ? null : addtime.trim();
    }

    public String getAddip() {
        return addip;
    }

    public void setAddip(String addip) {
        this.addip = addip == null ? null : addip.trim();
    }
}