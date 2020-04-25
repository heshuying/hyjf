package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class ActivityBillionSecondTime implements Serializable {
    private Integer id;

    private Integer accordPoint;

    private Integer accordTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccordPoint() {
        return accordPoint;
    }

    public void setAccordPoint(Integer accordPoint) {
        this.accordPoint = accordPoint;
    }

    public Integer getAccordTime() {
        return accordTime;
    }

    public void setAccordTime(Integer accordTime) {
        this.accordTime = accordTime;
    }
}