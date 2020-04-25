/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.model.customize.batch;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author fq
 * @version BatchActivityQixiCustomize, v0.1 2018/7/24 14:58
 */
public class BatchActivityQixiCustomize implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userId;

    private String username;
    /** 用户出借总额(散标+计划) */
    private BigDecimal money;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
