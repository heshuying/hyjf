package com.hyjf.admin.manager.content.operationreport;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.report.OperationReportCustomize;

import java.io.Serializable;
import java.util.List;

public class OperationReportBean extends OperationReportCustomize implements Serializable {

    private String ids;
    /**
     * 前台时间接收
     */
    private String startCreate;

    private String endCreate;

    private String typeSearch;

    private List<OperationReportCustomize> recordList;

    private String   pageStatus;
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

    public String getStartCreate() {
        return startCreate;
    }

    public void setStartCreate(String startCreate) {
        this.startCreate = startCreate;
    }

    public String getEndCreate() {
        return endCreate;
    }

    public void setEndCreate(String endCreate) {
        this.endCreate = endCreate;
    }

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public List<OperationReportCustomize> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<OperationReportCustomize> recordList) {
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

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getPageStatus() {
        return pageStatus;
    }

    public void setPageStatus(String pageStatus) {
        this.pageStatus = pageStatus;
    }
}
