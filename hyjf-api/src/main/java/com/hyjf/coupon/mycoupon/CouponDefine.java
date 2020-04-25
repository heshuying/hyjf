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

package com.hyjf.coupon.mycoupon;

import com.hyjf.base.bean.BaseDefine;

public class CouponDefine extends BaseDefine {

    /** 用户出借 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/coupon";
    /** 获取用户优惠券列表 @RequestMapping值*/
    public static final String GET_USERCOUPON = "/getUserCoupons";
    /** 获取用户优惠券详情 @RequestMapping值*/
    public static final String GET_USERCOUPON_DETAIL = "/getUserCouponDetail";
    
    /** 优惠券详情的路径 */
    public static final String COUPON_DETAIL_PATH = "coupon/detail/couponDetail";
    
    /** 错误页面 @Path值 */
    public static final String ERROR_PTAH = "error";
}
