package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class CertBorrow implements Serializable {
    private Integer id;

    private String borrowNid;

    private Integer borrowUserId;

    private Integer isUserInfo;

    private Integer isScatter;

    private Integer isStatus;

    private Integer isRepayPlan;

    private Integer isCredit;

    private Integer isTransfer;

    private Integer isTransferStatus;

    private Integer isUndertake;

    private Integer isTransact;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBorrowNid() {
        return borrowNid;
    }

    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid == null ? null : borrowNid.trim();
    }

    public Integer getBorrowUserId() {
        return borrowUserId;
    }

    public void setBorrowUserId(Integer borrowUserId) {
        this.borrowUserId = borrowUserId;
    }

    public Integer getIsUserInfo() {
        return isUserInfo;
    }

    public void setIsUserInfo(Integer isUserInfo) {
        this.isUserInfo = isUserInfo;
    }

    public Integer getIsScatter() {
        return isScatter;
    }

    public void setIsScatter(Integer isScatter) {
        this.isScatter = isScatter;
    }

    public Integer getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(Integer isStatus) {
        this.isStatus = isStatus;
    }

    public Integer getIsRepayPlan() {
        return isRepayPlan;
    }

    public void setIsRepayPlan(Integer isRepayPlan) {
        this.isRepayPlan = isRepayPlan;
    }

    public Integer getIsCredit() {
        return isCredit;
    }

    public void setIsCredit(Integer isCredit) {
        this.isCredit = isCredit;
    }

    public Integer getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(Integer isTransfer) {
        this.isTransfer = isTransfer;
    }

    public Integer getIsTransferStatus() {
        return isTransferStatus;
    }

    public void setIsTransferStatus(Integer isTransferStatus) {
        this.isTransferStatus = isTransferStatus;
    }

    public Integer getIsUndertake() {
        return isUndertake;
    }

    public void setIsUndertake(Integer isUndertake) {
        this.isUndertake = isUndertake;
    }

    public Integer getIsTransact() {
        return isTransact;
    }

    public void setIsTransact(Integer isTransact) {
        this.isTransact = isTransact;
    }
}