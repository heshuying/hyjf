package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class UserCorner implements Serializable {
    private Integer id;

    private Integer corner;

    private String sign;

    private Integer userId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCorner() {
        return corner;
    }

    public void setCorner(Integer corner) {
        this.corner = corner;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign == null ? null : sign.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}