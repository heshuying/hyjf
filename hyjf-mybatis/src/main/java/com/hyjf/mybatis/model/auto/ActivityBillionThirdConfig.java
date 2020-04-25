package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class ActivityBillionThirdConfig implements Serializable {
    private Integer id;

    private String secKillTime;

    private String prizeName;

    private String picUrl;

    private Integer couponNum;

    private String couponCode;

    private Integer secKillNum;

    private Integer status;

    private Integer killStatus;

    private Integer createTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSecKillTime() {
        return secKillTime;
    }

    public void setSecKillTime(String secKillTime) {
        this.secKillTime = secKillTime == null ? null : secKillTime.trim();
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName == null ? null : prizeName.trim();
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public Integer getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(Integer couponNum) {
        this.couponNum = couponNum;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode == null ? null : couponCode.trim();
    }

    public Integer getSecKillNum() {
        return secKillNum;
    }

    public void setSecKillNum(Integer secKillNum) {
        this.secKillNum = secKillNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getKillStatus() {
        return killStatus;
    }

    public void setKillStatus(Integer killStatus) {
        this.killStatus = killStatus;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }
}