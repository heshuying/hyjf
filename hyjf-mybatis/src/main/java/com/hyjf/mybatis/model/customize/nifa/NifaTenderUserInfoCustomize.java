/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.model.customize.nifa;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author PC-LIUSHOUYI
 * @version NifaTenderUserInfoCustomize, v0.1 2018/12/25 15:47
 */
public class NifaTenderUserInfoCustomize implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private BigDecimal loanFee;

    private BigDecimal account;

    private String idcard;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getLoanFee() {
        return loanFee;
    }

    public void setLoanFee(BigDecimal loanFee) {
        this.loanFee = loanFee;
    }

    public BigDecimal getAccount() {
        return account;
    }

    public void setAccount(BigDecimal account) {
        this.account = account;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

}
