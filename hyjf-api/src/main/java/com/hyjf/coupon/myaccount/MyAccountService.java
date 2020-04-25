package com.hyjf.coupon.myaccount;

public interface MyAccountService {

    VipInfoResultBean getVipInfo(Integer userId);

    CouponInfoResultBean getCouponInfo(Integer userId);

}
