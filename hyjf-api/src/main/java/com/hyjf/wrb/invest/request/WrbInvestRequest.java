package com.hyjf.wrb.invest.request;

public class WrbInvestRequest {

    /** 出借日期 */
    private String invest_date;

    /** 条数 */
    private Integer limit;

    /** 页数 */
    private Integer page;

    public String getInvest_date() {
        return invest_date;
    }

    public void setInvest_date(String invest_date) {
        this.invest_date = invest_date;
    }

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
