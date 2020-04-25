package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DataErrorDebt implements Serializable {
    private Integer id;

    private String bankId;

    private String batchId;

    private Integer borrowUserId;

    private Integer tenUserId;

    private String debtHolderAcc;

    private String prodIssuer;

    private Integer borrowId;

    private String serialNum;

    private BigDecimal amount;

    private BigDecimal interestWait;

    private BigDecimal interestPaid;

    private String debtObtDate;

    private String interestDate;

    private Integer intPayStyle;

    private String intPayDate;

    private String endDate;

    private BigDecimal expectAnualRate;

    private String currType;

    private String name;

    private String revers;

    private String retCode;

    private String authCode;

    private String errorDesc;

    private Integer tenderType;

    private String orderId;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId == null ? null : bankId.trim();
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId == null ? null : batchId.trim();
    }

    public Integer getBorrowUserId() {
        return borrowUserId;
    }

    public void setBorrowUserId(Integer borrowUserId) {
        this.borrowUserId = borrowUserId;
    }

    public Integer getTenUserId() {
        return tenUserId;
    }

    public void setTenUserId(Integer tenUserId) {
        this.tenUserId = tenUserId;
    }

    public String getDebtHolderAcc() {
        return debtHolderAcc;
    }

    public void setDebtHolderAcc(String debtHolderAcc) {
        this.debtHolderAcc = debtHolderAcc == null ? null : debtHolderAcc.trim();
    }

    public String getProdIssuer() {
        return prodIssuer;
    }

    public void setProdIssuer(String prodIssuer) {
        this.prodIssuer = prodIssuer == null ? null : prodIssuer.trim();
    }

    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum == null ? null : serialNum.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getInterestWait() {
        return interestWait;
    }

    public void setInterestWait(BigDecimal interestWait) {
        this.interestWait = interestWait;
    }

    public BigDecimal getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(BigDecimal interestPaid) {
        this.interestPaid = interestPaid;
    }

    public String getDebtObtDate() {
        return debtObtDate;
    }

    public void setDebtObtDate(String debtObtDate) {
        this.debtObtDate = debtObtDate == null ? null : debtObtDate.trim();
    }

    public String getInterestDate() {
        return interestDate;
    }

    public void setInterestDate(String interestDate) {
        this.interestDate = interestDate == null ? null : interestDate.trim();
    }

    public Integer getIntPayStyle() {
        return intPayStyle;
    }

    public void setIntPayStyle(Integer intPayStyle) {
        this.intPayStyle = intPayStyle;
    }

    public String getIntPayDate() {
        return intPayDate;
    }

    public void setIntPayDate(String intPayDate) {
        this.intPayDate = intPayDate == null ? null : intPayDate.trim();
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate == null ? null : endDate.trim();
    }

    public BigDecimal getExpectAnualRate() {
        return expectAnualRate;
    }

    public void setExpectAnualRate(BigDecimal expectAnualRate) {
        this.expectAnualRate = expectAnualRate;
    }

    public String getCurrType() {
        return currType;
    }

    public void setCurrType(String currType) {
        this.currType = currType == null ? null : currType.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getRevers() {
        return revers;
    }

    public void setRevers(String revers) {
        this.revers = revers == null ? null : revers.trim();
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode == null ? null : retCode.trim();
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode == null ? null : authCode.trim();
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc == null ? null : errorDesc.trim();
    }

    public Integer getTenderType() {
        return tenderType;
    }

    public void setTenderType(Integer tenderType) {
        this.tenderType = tenderType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
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