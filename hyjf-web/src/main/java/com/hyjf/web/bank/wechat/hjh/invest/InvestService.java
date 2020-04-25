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

package com.hyjf.web.bank.wechat.hjh.invest;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.web.BaseService;

@Service("wxhjhInvestService")
public interface InvestService extends BaseService {

	 /**
     * 
     * 获取用户优惠券id获得优惠券信息
     * 
     * @author pcc
     * @param couponId
     * @return
     */
    public UserCouponConfigCustomize getBestCouponById(String couponId);
    
    /**
     * 取得用户优惠券信息
     * 
     * @param couponGrantId
     * @param userId
     * @return
     */
    CouponConfigCustomizeV2 getCouponUser(String couponGrantId, int userId);
}
