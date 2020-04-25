package com.hyjf.mybatis.model.customize.wrb;

import java.math.BigDecimal;

/**
 * @author xiasq
 * @version WrbBorrowTenderCustomize, v0.1 2018/3/9 11:47
 * 标的出借明细
 */
public class WrbBorrowTenderCustomize {
    // 出借订单号
    private String nid;
    // 出借用户id
    private String userId;
    // 出借用户名
    private String username;
    // 出借金额
    private BigDecimal account;
    //出借时间
    private String investTime;

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getAccount() {
        return account;
    }

    public void setAccount(BigDecimal account) {
        this.account = account;
    }

    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }
}
