package com.hyjf.app.user.credit;

import java.io.Serializable;

import com.hyjf.mybatis.model.customize.app.AppTenderToCreditDetailCustomize;

public class AppTenderCreditDetailBean extends AppTenderToCreditDetailCustomize implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 6686511933010994456L;

    /**
     * 点击原项目跳转URL
     */
    private String borrowInfoUrl;

    /* 1债转说明 tabOneName */
    private String tabOneName;

    /* 债转说明Url tabOneUrl */
    private String tabOneUrl;

    /* 2出借记录 tabTwoName */
    private String tabTwoName;

    /* 出借记录url tabTwoUrl */
    private String tabTwoUrl;

    /** 用户出借url */
    private String investUrl;

    public String getTabOneName() {
        return tabOneName;
    }

    public void setTabOneName(String tabOneName) {
        this.tabOneName = tabOneName;
    }

    public String getTabOneUrl() {
        return tabOneUrl;
    }

    public void setTabOneUrl(String tabOneUrl) {
        this.tabOneUrl = tabOneUrl;
    }

    public String getTabTwoName() {
        return tabTwoName;
    }

    public void setTabTwoName(String tabTwoName) {
        this.tabTwoName = tabTwoName;
    }

    public String getTabTwoUrl() {
        return tabTwoUrl;
    }

    public void setTabTwoUrl(String tabTwoUrl) {
        this.tabTwoUrl = tabTwoUrl;
    }

    public String getInvestUrl() {
        return investUrl;
    }

    public void setInvestUrl(String investUrl) {
        this.investUrl = investUrl;
    }

    public String getBorrowInfoUrl() {
        return borrowInfoUrl;
    }

    public void setBorrowInfoUrl(String borrowInfoUrl) {
        this.borrowInfoUrl = borrowInfoUrl;
    }

}
