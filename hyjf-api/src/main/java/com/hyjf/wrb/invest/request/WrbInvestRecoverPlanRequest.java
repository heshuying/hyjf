package com.hyjf.wrb.invest.request;

public class WrbInvestRecoverPlanRequest {

    /** 平台用户id */
    private String pf_user_id;

    /** 出借记录id */
    private String invest_record_id;

    /** 项目id */
    private String bid_id;

    public String getPf_user_id() {
        return pf_user_id;
    }

    public void setPf_user_id(String pf_user_id) {
        this.pf_user_id = pf_user_id;
    }

    public String getInvest_record_id() {
        return invest_record_id;
    }

    public void setInvest_record_id(String invest_record_id) {
        this.invest_record_id = invest_record_id;
    }

    public String getBid_id() {
        return bid_id;
    }

    public void setBid_id(String bid_id) {
        this.bid_id = bid_id;
    }
}
