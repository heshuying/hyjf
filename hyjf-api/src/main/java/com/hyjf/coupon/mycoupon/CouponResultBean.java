package com.hyjf.coupon.mycoupon;

import com.hyjf.base.bean.BaseResultBean;

/**
 * 
 * 此处为类说明
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年8月16日
 * @see 下午3:04:43
 */
public class CouponResultBean extends BaseResultBean {
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;

    /**
     * 优惠券列表总记录数
     */
    private String couponTotal;
    
    /**
     * 优惠券列表状态
     */
    private String couponStatus;

    public String getCouponTotal() {
        return couponTotal;
    }

    public void setCouponTotal(String couponTotal) {
        this.couponTotal = couponTotal;
    }

    public String getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(String couponStatus) {
        this.couponStatus = couponStatus;
    }

    
}
