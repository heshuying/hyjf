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

package com.hyjf.coupon.myaccount;

import com.hyjf.base.bean.BaseDefine;

public class MyAccountDefine extends BaseDefine {

    /** 用户出借 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/myaccount";
    /** 获取用户优惠券列表 @RequestMapping值*/
    public static final String GET_VIP_INFO = "/getUserVIPInfo";
    /** 获取用户优惠券详情 @RequestMapping值*/
    public static final String GET_COUPON_INFO = "/getCouponInfo";
    
    /** 错误页面 @Path值 */
    public static final String ERROR_PTAH = "error";
}
