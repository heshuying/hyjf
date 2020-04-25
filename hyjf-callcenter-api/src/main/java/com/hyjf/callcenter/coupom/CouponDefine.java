/**
 * Description:江西银行账户常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 刘彬
 * @version: 1.0
 *           Created at: 2017年07月07日 下午2:33:39
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.callcenter.coupom;

import com.hyjf.callcenter.base.BaseDefine;

public class CouponDefine extends BaseDefine {

    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/coupon";
   
    /** 按照用户名/手机号查询优惠券 */
    public static final String GET_USER_COUPON_INFO_LIST_ACTION = "getUserCouponInfoList";
    
    /** 按照用户名/手机号查询优惠券使用（直投产品）*/
    public static final String GET_USER_COUPON_TENDER_LIST_ACTION = "getUserCouponTenderList";
    
    /** 按照用户名/手机号查询优惠券回款（直投产品）*/
    public static final String GET_USER_COUPON_BACK_MONEY_LIST_ACTION = "getUserCouponBackMoneyList";
    
    
}
