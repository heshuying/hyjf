/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.web.user.withdraw;

import com.hyjf.web.WebBaseAjaxResultBean;

/**
 * 此处为类说明
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月20日
 * @see 下午2:14:25
 */
public class UserWithdrawInFoResult extends WebBaseAjaxResultBean {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 7241847311843973976L;

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getShowBalance() {
        return showBalance;
    }

    public void setShowBalance(String showBalance) {
        this.showBalance = showBalance;
    }

    // 手续费
    private String fee;

    // 实际到账金额
    private String balance;

    // 提现金额
    private String showBalance;

}
