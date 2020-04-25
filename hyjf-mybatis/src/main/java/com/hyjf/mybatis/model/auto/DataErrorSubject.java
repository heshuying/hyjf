package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DataErrorSubject implements Serializable {
    private Integer id;

    private Integer borrowId;

    private String borrowNid;

    private String borrowDesc;

    private String borrowAccountId;

    private BigDecimal amount;

    private Integer paymentType;

    private Integer loanTerm;

    private BigDecimal borrowApr;

    private String guaranteeAccountId;

    private String raiseDate;

    private String raiseEndDate;

    private String respCode;

    private String errorDesc;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    public String getBorrowNid() {
        return borrowNid;
    }

    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid == null ? null : borrowNid.trim();
    }

    public String getBorrowDesc() {
        return borrowDesc;
    }

    public void setBorrowDesc(String borrowDesc) {
        this.borrowDesc = borrowDesc == null ? null : borrowDesc.trim();
    }

    public String getBorrowAccountId() {
        return borrowAccountId;
    }

    public void setBorrowAccountId(String borrowAccountId) {
        this.borrowAccountId = borrowAccountId == null ? null : borrowAccountId.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    public BigDecimal getBorrowApr() {
        return borrowApr;
    }

    public void setBorrowApr(BigDecimal borrowApr) {
        this.borrowApr = borrowApr;
    }

    public String getGuaranteeAccountId() {
        return guaranteeAccountId;
    }

    public void setGuaranteeAccountId(String guaranteeAccountId) {
        this.guaranteeAccountId = guaranteeAccountId == null ? null : guaranteeAccountId.trim();
    }

    public String getRaiseDate() {
        return raiseDate;
    }

    public void setRaiseDate(String raiseDate) {
        this.raiseDate = raiseDate == null ? null : raiseDate.trim();
    }

    public String getRaiseEndDate() {
        return raiseEndDate;
    }

    public void setRaiseEndDate(String raiseEndDate) {
        this.raiseEndDate = raiseEndDate == null ? null : raiseEndDate.trim();
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode == null ? null : respCode.trim();
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc == null ? null : errorDesc.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}