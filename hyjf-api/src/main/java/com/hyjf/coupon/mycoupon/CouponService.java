/**
 * Description:用户出借服务
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:45:13
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.coupon.mycoupon;

import java.util.List;
import java.util.Map;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.customize.coupon.CouponRecoverCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderDetailCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponUserForAppCustomize;

public interface CouponService extends BaseService {

    /**
     * 获取用户的优惠券列表
     * @return
     */
    List<CouponUserForAppCustomize> getCouponUserList(Map<String, Object> paraMap);
    
    /**
     * 用户的优惠券总数
     * @author hsy
     * @param couponUserCustomize
     * @return
     */
    Integer countCouponUsers(Map<String, Object> paraMap);

    CouponTenderDetailCustomize getUserCouponDetail(Map<String,Object> paramMap);

    List<CouponRecoverCustomize> getUserCouponRecover(Map<String,Object> paramMap);

    int updateCouponReadFlag(Integer userId, Integer readFlag);

}
