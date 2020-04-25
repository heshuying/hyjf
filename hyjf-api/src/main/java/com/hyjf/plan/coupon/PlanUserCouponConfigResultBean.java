package com.hyjf.plan.coupon;

import java.io.Serializable;

import com.hyjf.base.bean.BaseResultBean;

public class PlanUserCouponConfigResultBean extends BaseResultBean implements Serializable{
    /**
     * serialVersionUID:
     */

    private static final long serialVersionUID = 1L;
    
    private String couponConfigJson;
    
    private String couponInterest="0";

    public String getCouponConfigJson() {
        return couponConfigJson;
    }

    public void setCouponConfigJson(String couponConfigJson) {
        this.couponConfigJson = couponConfigJson;
    }

    public String getCouponInterest() {
        return couponInterest;
    }

    public void setCouponInterest(String couponInterest) {
        this.couponInterest = couponInterest;
    }
    
    
}
