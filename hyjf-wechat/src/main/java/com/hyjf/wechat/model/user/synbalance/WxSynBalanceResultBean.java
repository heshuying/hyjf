package com.hyjf.wechat.model.user.synbalance;

import com.hyjf.wechat.base.BaseResultBean;

public class WxSynBalanceResultBean extends BaseResultBean {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 8959137696050695655L;
    //余额数据
    private String bankBalance;
    //账户总额数据
    private String bankTotal;
    public String getBankBalance() {
        return bankBalance;
    }
    public void setBankBalance(String bankBalance) {
        this.bankBalance = bankBalance;
    }
    public String getBankTotal() {
        return bankTotal;
    }
    public void setBankTotal(String bankTotal) {
        this.bankTotal = bankTotal;
    }
    
    
    
    
}
