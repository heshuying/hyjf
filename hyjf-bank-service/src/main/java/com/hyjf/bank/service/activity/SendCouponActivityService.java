/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.activity;

/**
 * @author yinhui
 * @version SendCouponActivityService, v0.1 2018/10/16 15:55
 */
public interface SendCouponActivityService {

    void sendCoupon(Integer userId,String couponId,Integer rewardId);
}
