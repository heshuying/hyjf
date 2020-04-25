package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class BorrowFinserCharge implements Serializable {
    private String chargeCd;

    private String chargeType;

    private Integer chargeTime;

    private String chargeTimeType;

    private String chargeRate;

    private Integer projectType;

    private Integer status;

    private String remark;

    private Integer createTime;

    private Integer updateTime;

    private static final long serialVersionUID = 1L;

    public String getChargeCd() {
        return chargeCd;
    }

    public void setChargeCd(String chargeCd) {
        this.chargeCd = chargeCd == null ? null : chargeCd.trim();
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType == null ? null : chargeType.trim();
    }

    public Integer getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(Integer chargeTime) {
        this.chargeTime = chargeTime;
    }

    public String getChargeTimeType() {
        return chargeTimeType;
    }

    public void setChargeTimeType(String chargeTimeType) {
        this.chargeTimeType = chargeTimeType == null ? null : chargeTimeType.trim();
    }

    public String getChargeRate() {
        return chargeRate;
    }

    public void setChargeRate(String chargeRate) {
        this.chargeRate = chargeRate == null ? null : chargeRate.trim();
    }

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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