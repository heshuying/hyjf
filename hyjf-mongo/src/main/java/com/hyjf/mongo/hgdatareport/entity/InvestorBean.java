/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.hgdatareport.entity;

import com.hyjf.mongo.hgdatareport.base.BaseHgDataReportEntity;

import java.io.Serializable;

/**
 * @author jun
 * @version InvestorEntity, v0.1 2018/12/7 14:52
 */
public class InvestorBean implements Serializable {

    private String invest_amt;

    private String investor_name_idcard_digest;

    public void setInvest_amt(String invest_amt) {
        this.invest_amt = invest_amt;
    }

    public String getInvest_amt() {
        return invest_amt;
    }

    public void setInvestor_name_idcard_digest(String investor_name_idcard_digest) {
        this.investor_name_idcard_digest = investor_name_idcard_digest;
    }

    public String getInvestor_name_idcard_digest() {
        return investor_name_idcard_digest;
    }
}
