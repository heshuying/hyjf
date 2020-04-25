/**
 * Description:出借常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:33:39
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.plan.coupon;

import com.hyjf.base.bean.BaseDefine;

public class PlanCouponDefine extends BaseDefine {

    /** vip @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/plan/coupon";
    /** 获取用户优惠券总张数*/
    public static final String COUNT_COUPON_USERS = "countCouponUsers";
    
    /** 获取用户可用优惠券总张数*/
    public static final String GET_USER_COUPON_AVAILABLE_COUNT = "getUserCouponAvailableCount";
    /** 根据borrowNid和用户id获取用户可用优惠券和不可用优惠券列表*/
    public static final String GET_PROJECT_AVAILABLE_USER_COUPON_ACTION = "getProjectAvailableUserCoupon";
    

    /** 获取最优优惠券信息*/
    public static final String GET_BEST_COUPON = "getBestCoupon";
    /** 计算优惠券收益*/
    public static final String GET_COUPON_INTEREST = "getCouponInterest";
    
    /** 优惠券出借校验*/
    public static final String VALIDATE_COUPON_ACTION = "validateCoupon";
    
    /** 优惠券单独出借*/
    public static final String COUPON_TENDER_ACTION = "couponTender";
    
    /** 更新优惠券收益及还款时间*/
    public static final String COUPON_RECOVER_HTJ_ACTION = "updateCouponRecoverHtj";
    
    
    
}
