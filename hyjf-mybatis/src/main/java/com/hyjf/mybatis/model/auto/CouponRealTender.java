package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class CouponRealTender implements Serializable {
    private Integer id;

    private String couponTenderId;

    private String realTenderId;

    private Integer addTime;

    private String addUser;

    private Integer updateTime;

    private String updateUser;

    private Integer delFlg;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCouponTenderId() {
        return couponTenderId;
    }

    public void setCouponTenderId(String couponTenderId) {
        this.couponTenderId = couponTenderId == null ? null : couponTenderId.trim();
    }

    public String getRealTenderId() {
        return realTenderId;
    }

    public void setRealTenderId(String realTenderId) {
        this.realTenderId = realTenderId == null ? null : realTenderId.trim();
    }

    public Integer getAddTime() {
        return addTime;
    }

    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    public String getAddUser() {
        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser == null ? null : addUser.trim();
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }
}