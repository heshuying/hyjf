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

package com.hyjf.web.bank.wechat.hjh.coupon;

import com.hyjf.web.BaseDefine;

public class CouponDefine extends BaseDefine {

    /** 用户出借 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/wx/coupon";
    /** 根据borrowNid和用户id获取用户可用优惠券和不可用优惠券列表 @RequestMapping值 */
    public static final String GET_PROJECT_AVAILABLE_USER_COUPON_ACTION = "/getProjectAvailableUserCoupon";
    /** 获取用户优惠券列表 @RequestMapping值*/
    public static final String GET_USERCOUPON = "/getUserCoupons";
    /** 获取用户优惠券详情 @RequestMapping值*/
    public static final String GET_USERCOUPON_DETAIL = "/getUserCouponDetail";
    /** 活动弹出页面初始化 @RequestMapping值*/
    public static final String COUPON_ACTIVE_INIT = "/couponActiveInit";
    
    /** 注册送体验金活动校验 @RequestMapping值*/
    public static final String REGISTER_ACTIVE_CHECK = "/registerActiveCheck";
    
    /** 评测送加息券活动校验 @RequestMapping值*/
    public static final String EVALUATE_ACTIVE_CHECK = "/evaluateActiveCheck";
    
    
    /** 优惠券详情的路径 */
    public static final String COUPON_DETAIL_PATH = "coupon/detail/couponDetail";
    /** 优惠券详情的路径 */
    public static final String ACTIVE_INIT_PATH = "active";
    
    /** 错误页面 @Path值 */
    public static final String ERROR_PTAH = "error";
}
