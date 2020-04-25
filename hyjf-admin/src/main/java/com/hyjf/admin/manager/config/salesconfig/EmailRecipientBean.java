package com.hyjf.admin.manager.config.salesconfig;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.SellDailyDistribution;
import com.hyjf.mybatis.model.customize.SellDailyDistributionCustomize;

import java.util.List;

/**
 * @author lisheng
 * @version EmailRecipientBean, v0.1 2018/7/23 16:43
 */

public class EmailRecipientBean extends SellDailyDistributionCustomize {
    /**
     * serialVersionUID
     */

    private static final long serialVersionUID = 387630498860089653L;
    private String timeStartCreateSrch;
    private String timeEndCreateSrch;
    private String timeStartUpdateSrch;
    private String timeEndUpdateSrch;

    private List<SellDailyDistributionCustomize> recordList;
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

    public List<SellDailyDistributionCustomize> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<SellDailyDistributionCustomize> recordList) {
        this.recordList = recordList;
    }

    public String getTimeStartCreateSrch() {
        return timeStartCreateSrch;
    }

    public void setTimeStartCreateSrch(String timeStartCreateSrch) {
        this.timeStartCreateSrch = timeStartCreateSrch;
    }

    public String getTimeEndCreateSrch() {
        return timeEndCreateSrch;
    }

    public void setTimeEndCreateSrch(String timeEndCreateSrch) {
        this.timeEndCreateSrch = timeEndCreateSrch;
    }

    public String getTimeStartUpdateSrch() {
        return timeStartUpdateSrch;
    }

    public void setTimeStartUpdateSrch(String timeStartUpdateSrch) {
        this.timeStartUpdateSrch = timeStartUpdateSrch;
    }

    public String getTimeEndUpdateSrch() {
        return timeEndUpdateSrch;
    }

    public void setTimeEndUpdateSrch(String timeEndUpdateSrch) {
        this.timeEndUpdateSrch = timeEndUpdateSrch;
    }

}
