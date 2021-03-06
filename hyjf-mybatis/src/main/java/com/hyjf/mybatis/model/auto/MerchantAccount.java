package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;

public class MerchantAccount implements Serializable {
    private Integer id;

    private String subAccountName;

    private String subAccountType;

    private String subAccountCode;

    private Integer transferIntoFlg;

    private Integer transferOutFlg;

    private Long balanceLowerLimit;

    private Integer autoTransferOut;

    private Integer autoTransferInto;

    private Integer transferIntoRatio;

    private BigDecimal accountBalance;

    private BigDecimal availableBalance;

    private BigDecimal frost;

    private String purpose;

    private Short sort;

    private Integer createTime;

    private Integer updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubAccountName() {
        return subAccountName;
    }

    public void setSubAccountName(String subAccountName) {
        this.subAccountName = subAccountName == null ? null : subAccountName.trim();
    }

    public String getSubAccountType() {
        return subAccountType;
    }

    public void setSubAccountType(String subAccountType) {
        this.subAccountType = subAccountType == null ? null : subAccountType.trim();
    }

    public String getSubAccountCode() {
        return subAccountCode;
    }

    public void setSubAccountCode(String subAccountCode) {
        this.subAccountCode = subAccountCode == null ? null : subAccountCode.trim();
    }

    public Integer getTransferIntoFlg() {
        return transferIntoFlg;
    }

    public void setTransferIntoFlg(Integer transferIntoFlg) {
        this.transferIntoFlg = transferIntoFlg;
    }

    public Integer getTransferOutFlg() {
        return transferOutFlg;
    }

    public void setTransferOutFlg(Integer transferOutFlg) {
        this.transferOutFlg = transferOutFlg;
    }

    public Long getBalanceLowerLimit() {
        return balanceLowerLimit;
    }

    public void setBalanceLowerLimit(Long balanceLowerLimit) {
        this.balanceLowerLimit = balanceLowerLimit;
    }

    public Integer getAutoTransferOut() {
        return autoTransferOut;
    }

    public void setAutoTransferOut(Integer autoTransferOut) {
        this.autoTransferOut = autoTransferOut;
    }

    public Integer getAutoTransferInto() {
        return autoTransferInto;
    }

    public void setAutoTransferInto(Integer autoTransferInto) {
        this.autoTransferInto = autoTransferInto;
    }

    public Integer getTransferIntoRatio() {
        return transferIntoRatio;
    }

    public void setTransferIntoRatio(Integer transferIntoRatio) {
        this.transferIntoRatio = transferIntoRatio;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getFrost() {
        return frost;
    }

    public void setFrost(BigDecimal frost) {
        this.frost = frost;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose == null ? null : purpose.trim();
    }

    public Short getSort() {
        return sort;
    }

    public void setSort(Short sort) {
        this.sort = sort;
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
}