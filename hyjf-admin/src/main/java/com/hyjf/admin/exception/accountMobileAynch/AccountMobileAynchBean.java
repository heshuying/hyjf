package com.hyjf.admin.exception.accountMobileAynch;


import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.AccountMobileAynch;

import java.util.List;

/**
 * @author B2
 * @version AccountMobileAynchBean, v0.1 2018/5/9 16:58
 */

public class AccountMobileAynchBean {
    private String username;

    private String account;

    private String newAccount;

    private String mobile;

    private String newMobile;

    private Integer status;

    private Integer flag;

    private String id;

    private String accountId;
    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;
    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;

    public List<AccountMobileAynch> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<AccountMobileAynch> recordList) {
        this.recordList = recordList;
    }

    public int getPaginatorPage() {
        if (paginatorPage == 0) {
            paginatorPage = 1;
        }
        return paginatorPage;
    }

    private List<AccountMobileAynch> recordList;

    public void setPaginatorPage(int paginatorPage) {
        this.paginatorPage = paginatorPage;
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNewAccount() {
        return newAccount;
    }

    public void setNewAccount(String newAccount) {
        this.newAccount = newAccount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNewMobile() {
        return newMobile;
    }

    public void setNewMobile(String newMobile) {
        this.newMobile = newMobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
