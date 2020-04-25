package com.hyjf.plan.coupon;

import java.io.Serializable;

import com.hyjf.base.bean.BaseResultBean;

public class PlanCountCouponUsersResultBean extends BaseResultBean implements Serializable  {
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -1729911855363180773L;
    private Integer recordTotal;
    public Integer getRecordTotal() {
        return recordTotal;
    }
    public void setRecordTotal(Integer recordTotal) {
        this.recordTotal = recordTotal;
    }

    

	
}
