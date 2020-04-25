package com.hyjf.bank.service.hjh.borrow.calculatefairvalue;

import java.io.Serializable;

public class HjhCalculateFairValueBean implements Serializable {

    private static final long serialVersionUID = -4063164049843296435L;

    // 加入订单号
    private String accedeOrderId;

    // 计算类型:0:清算,1:计算
    private Integer calculateType;

    public String getAccedeOrderId() {
        return accedeOrderId;
    }

    public void setAccedeOrderId(String accedeOrderId) {
        this.accedeOrderId = accedeOrderId;
    }

    public Integer getCalculateType() {
        return calculateType;
    }

    public void setCalculateType(Integer calculateType) {
        this.calculateType = calculateType;
    }
}
