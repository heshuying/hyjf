package com.hyjf.admin.manager.borrow.debtconfig;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.DebtConfig;
import com.hyjf.mybatis.model.auto.DebtConfigLog;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author tanyy
 * @date 2018/08/09 17:00
 * @version V1.0  
 */
public class DebtConfigBean extends DebtConfigLog implements Serializable {

    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;

    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;


    private List<DebtConfigLog> recordList;


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

    public List<DebtConfigLog> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<DebtConfigLog> recordList) {
        this.recordList = recordList;
    }
}
