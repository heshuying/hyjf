/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.app.user.recharge;

import com.hyjf.app.BaseResultBean;

/**
 * 此处为类说明
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年2月19日
 * @see 14:47:59
 */

@Deprecated
public class RechargeInfoResultVo extends BaseResultBean {

    private String fee = "";

    private String balance = "";

    private String logo = "";

    private String bank = "";

    private String cardNo = "";

    private String code = "";

    private String moneyInfo = "";

    private String rechargeRuleUrl = "";

    //add by xiashuqing 20171204 begin app改版2.1新增查询账户余额
    //可用金额
    private String availableAmount = "";
    //add by xiashuqing 20171204 begin app改版2.1新增查询账户余额


    public RechargeInfoResultVo(String request) {
        super(request);
    }

    public RechargeInfoResultVo(String request, String rechargeRuleUrl) {
        super(request);
        this.rechargeRuleUrl = rechargeRuleUrl;
    }

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -3095160356825014456L;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMoneyInfo() {
        return moneyInfo;
    }

    public void setMoneyInfo(String moneyInfo) {
        this.moneyInfo = moneyInfo;
    }

    public String getRechargeRuleUrl() {
        return rechargeRuleUrl;
    }

    public void setRechargeRuleUrl(String rechargeRuleUrl) {
        this.rechargeRuleUrl = rechargeRuleUrl;
    }

    public String getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(String availableAmount) {
        this.availableAmount = availableAmount;
    }
}
