package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TenderMonthInfo implements Serializable {
    private Integer id;

    private BigDecimal tenderMoney;

    private Integer tenderType;

    private String tenderTime;

    private Date addtime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getTenderMoney() {
        return tenderMoney;
    }

    public void setTenderMoney(BigDecimal tenderMoney) {
        this.tenderMoney = tenderMoney;
    }

    public Integer getTenderType() {
        return tenderType;
    }

    public void setTenderType(Integer tenderType) {
        this.tenderType = tenderType;
    }

    public String getTenderTime() {
        return tenderTime;
    }

    public void setTenderTime(String tenderTime) {
        this.tenderTime = tenderTime == null ? null : tenderTime.trim();
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}