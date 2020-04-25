package com.hyjf.admin.manager.config.banksetting.bankinterface;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.BankInterface;

import java.io.Serializable;
import java.util.List;


/**
 * 接口切换实体类
 * 
 * @author 
 *
 */
public class BankInterfaceBean extends BankInterface implements Serializable {


    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3803722754627032581L;

    private List<BankInterface> recordList;

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

    public void setPaginatorPage(int paginatorPage) {
        this.paginatorPage = paginatorPage;
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public List<BankInterface> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<BankInterface> recordList) {
        this.recordList = recordList;
    }

}
