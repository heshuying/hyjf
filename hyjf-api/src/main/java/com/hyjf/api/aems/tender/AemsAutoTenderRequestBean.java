package com.hyjf.api.aems.tender;

import com.hyjf.base.bean.BaseBean;

/**
 * 自动投资请求参数
 */
public class AemsAutoTenderRequestBean extends BaseBean {

    private String borrowNid;

    private String money;

    public String getBorrowNid() {
        return borrowNid;
    }

    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }


}
