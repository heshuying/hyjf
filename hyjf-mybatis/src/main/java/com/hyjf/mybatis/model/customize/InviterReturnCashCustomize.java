package com.hyjf.mybatis.model.customize;

import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.InviterReturnDetail;

import java.io.Serializable;
import java.math.BigDecimal;

public class InviterReturnCashCustomize extends InviterReturnDetail implements Serializable {

    private String refferName;
    private Integer refferId;

    public String getRefferName() {
        return refferName;
    }

    public void setRefferName(String refferName) {
        this.refferName = refferName;
    }

    public Integer getRefferId() {
        return refferId;
    }

    public void setRefferId(Integer refferId) {
        this.refferId = refferId;
    }
}