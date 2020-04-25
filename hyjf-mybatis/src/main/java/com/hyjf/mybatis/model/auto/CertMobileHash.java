package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class CertMobileHash implements Serializable {
    private Integer id;

    private Integer userId;

    private String mobile;

    private String salt;

    private String phonehash;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public String getPhonehash() {
        return phonehash;
    }

    public void setPhonehash(String phonehash) {
        this.phonehash = phonehash == null ? null : phonehash.trim();
    }
}