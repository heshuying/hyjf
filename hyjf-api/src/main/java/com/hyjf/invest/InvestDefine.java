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

package com.hyjf.invest;

import com.hyjf.base.bean.BaseDefine;

public class InvestDefine extends BaseDefine {

    /** vip @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/invest";
   
    /** 根据borrowNid和用户id获取用户可用优惠券和不可用优惠券列表 @RequestMapping值*/
    public static final String GET_PROJECT_AVAILABLE_USER_COUPON_ACTION = "/getProjectAvailableUserCoupon";
    /** 优惠券单独出借*/
    public static final String COUPON_TENDER_ACTION = "couponTender";
    
    /** 优惠券出借校验*/
    public static final String VALIDATE_COUPON_ACTION = "validateCoupon";
    /** 出借校验*/
    public static final String CHECK_PARAM_ACTION = "checkParam";
    
    
    /** 根据出借项目id获取出借信息*/
    public static final String GET_INVEST_INFO_MAPPING = "getInvestInfo";
    
    
    /** 预期收益文字描述常量 */
    public static final String PROSPECTIVE_EARNINGS = "预期收益 ";

}
