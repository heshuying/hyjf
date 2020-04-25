
package com.hyjf.wechat.controller.user.coupon;

public class WxCouponDefine {

    /** 用户出借 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/coupon";
    /** 根据borrowNid和用户id获取用户可用优惠券和不可用优惠券列表 @RequestMapping值 */
    public static final String GET_PROJECT_AVAILABLE_USER_COUPON_ACTION = "/getProjectAvailableUserCoupon";
    /** 汇计划 根据borrowNid和用户id获取用户可用优惠券和不可用优惠券列表 @RequestMapping值 */
    public static final String GET_PROJECT_AVAILABLE_USER_COUPON_HJH_ACTION = "/getProjectAvailableUserCouponHJH";
}
