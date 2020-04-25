package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;

public class FeerateModifyLog implements Serializable {
    private Integer id;

    private String instCode;

    private Integer assetType;

    private Integer borrowPeriod;

    private String borrowStyle;

    private BigDecimal borrowApr;

    private String serviceFee;

    private String manageFee;

    private String revenueDiffRate;

    private String lateInterestRate;

    private Integer lateFreeDays;

    private Integer status;

    private Integer modifyType;

    private Integer createUser;

    private Integer createTime;

    private Integer updateUser;

    private Integer updateTime;

    private Integer delFlg;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInstCode() {
        return instCode;
    }

    public void setInstCode(String instCode) {
        this.instCode = instCode == null ? null : instCode.trim();
    }

    public Integer getAssetType() {
        return assetType;
    }

    public void setAssetType(Integer assetType) {
        this.assetType = assetType;
    }

    public Integer getBorrowPeriod() {
        return borrowPeriod;
    }

    public void setBorrowPeriod(Integer borrowPeriod) {
        this.borrowPeriod = borrowPeriod;
    }

    public String getBorrowStyle() {
        return borrowStyle;
    }

    public void setBorrowStyle(String borrowStyle) {
        this.borrowStyle = borrowStyle == null ? null : borrowStyle.trim();
    }

    public BigDecimal getBorrowApr() {
        return borrowApr;
    }

    public void setBorrowApr(BigDecimal borrowApr) {
        this.borrowApr = borrowApr;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee == null ? null : serviceFee.trim();
    }

    public String getManageFee() {
        return manageFee;
    }

    public void setManageFee(String manageFee) {
        this.manageFee = manageFee == null ? null : manageFee.trim();
    }

    public String getRevenueDiffRate() {
        return revenueDiffRate;
    }

    public void setRevenueDiffRate(String revenueDiffRate) {
        this.revenueDiffRate = revenueDiffRate == null ? null : revenueDiffRate.trim();
    }

    public String getLateInterestRate() {
        return lateInterestRate;
    }

    public void setLateInterestRate(String lateInterestRate) {
        this.lateInterestRate = lateInterestRate == null ? null : lateInterestRate.trim();
    }

    public Integer getLateFreeDays() {
        return lateFreeDays;
    }

    public void setLateFreeDays(Integer lateFreeDays) {
        this.lateFreeDays = lateFreeDays;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getModifyType() {
        return modifyType;
    }

    public void setModifyType(Integer modifyType) {
        this.modifyType = modifyType;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }
}