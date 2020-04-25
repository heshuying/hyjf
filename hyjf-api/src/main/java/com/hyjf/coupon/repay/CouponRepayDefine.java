
package com.hyjf.coupon.repay;

import com.hyjf.base.bean.BaseDefine;

public class CouponRepayDefine extends BaseDefine {

    /** 优惠券还款 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/coupon/repay";
    /** 直投类优惠券自动还款 @RequestMapping值*/
    public static final String COUPON_REPAY_ACTION = "/borrowRepayForCoupon";
    /** 汇添金优惠券自动还款 @RequestMapping值*/
    public static final String COUPON_REPAY_HTJ_ACTION = "/couponRepayForHtj";
    /** 体验金按收益期限自动还款 @RequestMapping值*/
    public static final String COUPON_REPAY_COUPON_ONLY_ACTION = "/borrowRepayForCouponOnly";
}
