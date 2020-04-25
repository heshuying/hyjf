package com.hyjf.bank.service.user.creditend;

/**
 * Created by lb on 2018/3/5.
 */
public class CreditEndBean {
    private String accountId;//电子账号

    private String orderId;//订单号

    private String forAccountId;//对手电子账号

    private String productId;//标的号

    private String authCode;//授权码

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getForAccountId() {
        return forAccountId;
    }

    public void setForAccountId(String forAccountId) {
        this.forAccountId = forAccountId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
