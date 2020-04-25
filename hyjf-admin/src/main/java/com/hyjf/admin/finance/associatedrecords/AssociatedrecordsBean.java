package com.hyjf.admin.finance.associatedrecords;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.DirectionalTransferAssociatedRecords;

public class AssociatedrecordsBean extends DirectionalTransferAssociatedRecords implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 611158695937717183L;

    // 开始时间(检索用)
    private String startDate;

    // 结束时间(检索用)
    private String endDate;

    // 关联状态(检索用)
    private String statusSearch;

    // 关联记录list
    private List<DirectionalTransferAssociatedRecords> associatedRecordsList;

    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;

    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatusSearch() {
        return statusSearch;
    }

    public void setStatusSearch(String statusSearch) {
        this.statusSearch = statusSearch;
    }

    public List<DirectionalTransferAssociatedRecords> getAssociatedRecordsList() {
        return associatedRecordsList;
    }

    public void setAssociatedRecordsList(List<DirectionalTransferAssociatedRecords> associatedRecordsList) {
        this.associatedRecordsList = associatedRecordsList;
    }

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

}
