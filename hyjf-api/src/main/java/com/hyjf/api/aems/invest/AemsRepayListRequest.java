package com.hyjf.api.aems.invest;

import com.hyjf.base.bean.BaseBean;

public class AemsRepayListRequest extends BaseBean{

    /*开始时间（必填）*/
    private String startTime;
    /*结束时间（必填）*/
    private String endTime;
    /*电子账号（选填）*/
    private String accountId;
    /*标的编号（选填）*/
    private String borrowNid;

    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String getAccountId() {
        return accountId;
    }

    @Override
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getBorrowNid() {
        return borrowNid;
    }
    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }
}
