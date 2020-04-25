package com.hyjf.coupon.myaccount;

import com.hyjf.base.bean.BaseResultBean;

/**
 * 
 * 此处为类说明
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年8月17日
 * @see 下午5:17:26
 */
public class CouponInfoResultBean extends BaseResultBean {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -5106904408137296106L;
    
    private String readFlag; // 优惠券更新标识

    private String couponDescription = "福利多多哒"; // 优惠券描述

    private String isUpdate; // 我的账户更新标识(0未更新，1已更新)

    public String getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(String readFlag) {
        this.readFlag = readFlag;
    }

    public String getCouponDescription() {
        return couponDescription;
    }

    public void setCouponDescription(String couponDescription) {
        this.couponDescription = couponDescription;
    }

    public String getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(String isUpdate) {
        this.isUpdate = isUpdate;
    }
    
    
}
