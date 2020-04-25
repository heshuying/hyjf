package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;

public class PoundageException implements Serializable {
    private Integer id;

    private BigDecimal ledgerAmount;

    private Integer poundageId;

    private Integer ledgerId;

    private String payeeName;

    private Integer ledgerStatus;

    private Integer createTime;

    private Integer updateTime;

    private Integer updater;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getLedgerAmount() {
        return ledgerAmount;
    }

    public void setLedgerAmount(BigDecimal ledgerAmount) {
        this.ledgerAmount = ledgerAmount;
    }

    public Integer getPoundageId() {
        return poundageId;
    }

    public void setPoundageId(Integer poundageId) {
        this.poundageId = poundageId;
    }

    public Integer getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Integer ledgerId) {
        this.ledgerId = ledgerId;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName == null ? null : payeeName.trim();
    }

    public Integer getLedgerStatus() {
        return ledgerStatus;
    }

    public void setLedgerStatus(Integer ledgerStatus) {
        this.ledgerStatus = ledgerStatus;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdater() {
        return updater;
    }

    public void setUpdater(Integer updater) {
        this.updater = updater;
    }
}