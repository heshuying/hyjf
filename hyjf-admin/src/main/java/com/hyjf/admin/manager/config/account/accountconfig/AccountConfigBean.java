package com.hyjf.admin.manager.config.account.accountconfig;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.MerchantAccount;

public class AccountConfigBean extends MerchantAccount implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 2621108187566173147L;

    private String ids;

    private List<MerchantAccount> subAccountList;

    /** 子账户名称(检索用) */
    private String subAccountNameSear;

    /** 子账户类型(检索用) */
    private String subAccountTypeSear;

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

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public List<MerchantAccount> getSubAccountList() {
        return subAccountList;
    }

    public void setSubAccountList(List<MerchantAccount> subAccountList) {
        this.subAccountList = subAccountList;
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

}
