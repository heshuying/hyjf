package com.hyjf.app.user.credit;

import java.io.Serializable;

public class AppTenderCreditBean implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 2037793484782642762L;

    /**
     * 转让记录转让状态:0:转让中,1:转让成功,2:全部
     */
    private String status;

    /**
     * 翻页机能用的隐藏变量
     */
    private int page = 1;

    /**
     * 翻页功能所用分页大小
     */
    private int pageSize = 10;

    public int getPage() {
        if (page == 0) {
            page = 1;
        }
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
