package com.hyjf.admin.finance.bankjournal;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.EveLog;
import com.hyjf.mybatis.model.customize.EveLogCustomize;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cui on 2018/1/18.
 */
public class BankJournalBean extends EveLogCustomize implements Serializable{

    private List<EveLogCustomize> recordList;

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

    public List<EveLogCustomize> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<EveLogCustomize> recordList) {
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
