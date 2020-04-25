package com.hyjf.admin.manager.config.bailconfiglog;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.HjhBailConfigLog;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigInfoCustomize;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigLogCustomize;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 提成设置
 *
 * @author liushouyi
 */
public class BailConfigLogBean extends HjhBailConfigLog implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 387630498860089653L;

    private List<HjhBailConfigLogCustomize> recordList;

    private String instCodeSrch;

    private String modifyColumnSrch;

    private String createUserNameSrch;

    private String startDate;

    private String endDate;

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

    public List<HjhBailConfigLogCustomize> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<HjhBailConfigLogCustomize> recordList) {
        this.recordList = recordList;
    }

    public String getInstCodeSrch() {
        return instCodeSrch;
    }

    public void setInstCodeSrch(String instCodeSrch) {
        this.instCodeSrch = instCodeSrch;
    }

    public String getModifyColumnSrch() {
        return modifyColumnSrch;
    }

    public void setModifyColumnSrch(String modifyColumnSrch) {
        this.modifyColumnSrch = modifyColumnSrch;
    }

    public String getCreateUserNameSrch() {
        return createUserNameSrch;
    }

    public void setCreateUserNameSrch(String createUserNameSrch) {
        this.createUserNameSrch = createUserNameSrch;
    }

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
}
