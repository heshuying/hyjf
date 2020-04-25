package com.hyjf.wechat.service.coupon;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;

/**
 * Created by cuigq on 2018/2/23.
 */
public interface WxCouponService {
    /**
     * 查詢用户可用优惠券列表
     *
     * @param borrowNid
     * @param money
     */
    public Map<String, Object> getProjectAvailableUserCoupon(String borrowNid, String money, Integer userId);


    UserCouponConfigCustomize getCouponById(String couponId);


	void getHJHProjectAvailableUserCoupon(String platform, String planNid, Integer userId, JSONObject ret, String money);
}
