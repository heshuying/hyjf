package com.hyjf.admin.manager.config.account.accountbalance;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;

public class AccountBalanceMonitoringBean implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -1967959761793508714L;

    /** 余额监控一览 */
    private List<SubAccountListResult> accountBalanceList;

    /** 子账户名称(检索用) */
    private String subAccountNameSear;

    /** 子账户类型(检索用) */
    private String subAccountTypeSear;

    /** 列表数据JSON */
    private String balanceDataJson;

    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;

    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;

    public int getPaginatorPage() {
        return paginatorPage == 0 ? 1 : paginatorPage;
    }

    public void setPaginatorPage(int paginatorPage) {
        this.paginatorPage = paginatorPage;
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public List<SubAccountListResult> getAccountBalanceList() {
        return accountBalanceList;
    }

    public void setAccountBalanceList(List<SubAccountListResult> accountBalanceList) {
        this.accountBalanceList = accountBalanceList;
    }

    public String getSubAccountNameSear() {
        return subAccountNameSear;
    }

    public void setSubAccountNameSear(String subAccountNameSear) {
        this.subAccountNameSear = subAccountNameSear;
    }

    public String getSubAccountTypeSear() {
        return subAccountTypeSear;
    }

    public void setSubAccountTypeSear(String subAccountTypeSear) {
        this.subAccountTypeSear = subAccountTypeSear;
    }

    public String getBalanceDataJson() {
        return balanceDataJson;
    }

    public void setBalanceDataJson(String balanceDataJson) {
        this.balanceDataJson = balanceDataJson;
    }

}
