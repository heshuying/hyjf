package com.hyjf.bank.service.fdd.fdduserinfochange;

import java.io.Serializable;

/**
 * 法大大客户信息修改Bean
 *
 * @author liuyang
 */
public class FddUserInfoBean implements Serializable {

    private static final long serialVersionUID = -8663419795829173907L;

    // 用户ID
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
