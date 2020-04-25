package com.hyjf.coupon.mycoupon;

import java.util.List;

import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.mybatis.model.customize.coupon.CouponRecoverCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderDetailCustomize;

/**
 * 
 * 此处为类说明
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年8月16日
 * @see 下午3:04:43
 */
public class CouponDetailResultBean extends BaseResultBean {
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;
    
    //优惠券详情
    private CouponTenderDetailCustomize detail;
    
    private List<CouponRecoverCustomize> couponRecoverlist;
    
    private String receivedMoney;
    
    private String earnings;

    public CouponTenderDetailCustomize getDetail() {
        return detail;
    }

    public void setDetail(CouponTenderDetailCustomize detail) {
        this.detail = detail;
    }

    public List<CouponRecoverCustomize> getCouponRecoverlist() {
        return couponRecoverlist;
    }

    public void setCouponRecoverlist(List<CouponRecoverCustomize> couponRecoverlist) {
        this.couponRecoverlist = couponRecoverlist;
    }

    public String getReceivedMoney() {
        return receivedMoney;
    }

    public void setReceivedMoney(String receivedMoney) {
        this.receivedMoney = receivedMoney;
    }

    public String getEarnings() {
        return earnings;
    }

    public void setEarnings(String earnings) {
        this.earnings = earnings;
    }

}
