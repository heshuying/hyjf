package com.hyjf.wechat.model.borrow.result;

import com.hyjf.wechat.base.BaseResultBean;

public class WxTenderResultBean extends BaseResultBean {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 8959137696050695655L;
    //出借url
    private String tenderUrl;

    private String evalType;

    private String evalFlagType;

    private String revaluationMoney;

    private String revaluationMoneyPrincipal;

    public String getEvalFlagType() {
        return evalFlagType;
    }

    public void setEvalFlagType(String evalFlagType) {
        this.evalFlagType = evalFlagType;
    }

    public String getRevaluationMoneyPrincipal() {
        return revaluationMoneyPrincipal;
    }

    public void setRevaluationMoneyPrincipal(String revaluationMoneyPrincipal) {
        this.revaluationMoneyPrincipal = revaluationMoneyPrincipal;
    }

    public String getEvalType() {
        return evalType;
    }

    public void setEvalType(String evalType) {
        this.evalType = evalType;
    }

    public String getRevaluationMoney() {
        return revaluationMoney;
    }

    public void setRevaluationMoney(String revaluationMoney) {
        this.revaluationMoney = revaluationMoney;
    }

    public String getTenderUrl() {
        return tenderUrl;
    }

    public void setTenderUrl(String tenderUrl) {
        this.tenderUrl = tenderUrl;
    }
    
    
    
}
