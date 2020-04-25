/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.wechat.controller.activity.activity0601;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.wechat.base.BaseService;

/**
 * @author fuqiang
 * @version Act0601Service, v0.1 2018/5/25 11:19
 */
public interface Act0601Service extends BaseService {

    /**
     * 校验兑奖口令
     *
     * @param command 兑奖口令
     * @return
     */
    JSONObject selectCommand(String command, Integer userId, String couponId, String activity);

    /**
     * 发券
     *
     * @param userId
     * @param couponCode 优惠券code
     * @param activityId 活动id
     */
    JSONObject sendCoupon(Integer userId, String couponCode, String activityId);
}
