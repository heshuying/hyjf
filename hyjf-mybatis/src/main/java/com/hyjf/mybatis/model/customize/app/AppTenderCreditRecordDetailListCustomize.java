package com.hyjf.mybatis.model.customize.app;

import java.io.Serializable;

/**
 * 
 * 转让记录详情ListBean
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年8月4日
 * @see 上午10:15:16
 */
public class AppTenderCreditRecordDetailListCustomize implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -4862885394250634845L;

    /**
     * 承接人姓名
     */
    private String assignUserName;

    /**
     * 认购时间
     */
    private String addTime;

    /**
     * 认购本金
     */
    private String assignCapital;

    /**
     * 支付金额
     */
    private String assignPay;

    /**
     * 服务费
     */
    private String creditFee;

    /**
     * 实际到账金额
     */
    private String actualMoney;

    /**
     * 查看协议URL
     */
    private String url;

    public String getAssignUserName() {
        return assignUserName;
    }

    public void setAssignUserName(String assignUserName) {
        this.assignUserName = assignUserName;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getAssignPay() {
        return assignPay;
    }

    public void setAssignPay(String assignPay) {
        this.assignPay = assignPay;
    }

    public String getCreditFee() {
        return creditFee;
    }

    public void setCreditFee(String creditFee) {
        this.creditFee = creditFee;
    }

    public String getActualMoney() {
        return actualMoney;
    }

    public void setActualMoney(String actualMoney) {
        this.actualMoney = actualMoney;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAssignCapital() {
        return assignCapital;
    }

    public void setAssignCapital(String assignCapital) {
        this.assignCapital = assignCapital;
    }

}
