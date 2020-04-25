package com.hyjf.api.report;

import com.hyjf.base.bean.BaseBean;

/**
 * @author
 */
public class ReportBean extends BaseBean {

    /*运营报告主键*/
    private String id;
    private String isRelease;
    private String paginatorPage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsRelease() {
        return isRelease;
    }

    public void setIsRelease(String isRelease) {
        this.isRelease = isRelease;
    }

    public String getPaginatorPage() {
        return paginatorPage;
    }

    public void setPaginatorPage(String paginatorPage) {
        this.paginatorPage = paginatorPage;
    }
}
