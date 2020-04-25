package com.hyjf.admin.finance.bankaleve;

import com.google.common.collect.Lists;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.AleveLogCustomize;

import java.io.Serializable;
import java.util.List;

/**
 * 前台查询参数
 * 為什麽不把分頁參數抽象成基類？
 * Created by cuigq on 2018/1/22.
 */
public class BankAleveBean extends AleveLogCustomize implements Serializable {

    private List<AleveLogCustomize> recordList;

    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;
    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;

    public int getPaginatorPage() {
        if (paginatorPage == 0) {
            paginatorPage = 1;
        }
        return paginatorPage;
    }

    public List<AleveLogCustomize> getRecordList() {
        if (recordList == null) {
            recordList = Lists.newArrayList();
        }
        return recordList;
    }

    public void setRecordList(List<AleveLogCustomize> recordList) {
        this.recordList = recordList;
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
}
