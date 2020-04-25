package com.hyjf.wrb.noticeinfo.request;

public class WrbNoticeinfoRequest {
	
	 /** 条数 */
    private Integer limit;

    /** 页数 */
    private Integer page;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

}
