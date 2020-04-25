package com.hyjf.batch.coupon.timeout;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.log.LogUtil;

public class CouponTimeoutTask {
    /** 类名 */
    private static final String THIS_CLASS = CouponTimeoutTask.class.getName();


    @Autowired
    CouponTimeoutService couponTimeoutService;

    public void run() {
        // 检查收益是否过期
    	couponTimeout();
    }
    
    /**
     * 检查收益是否过期
     */
    private void couponTimeout(){
    	String methodName = "couponTimeout";
    	LogUtil.startLog(THIS_CLASS, methodName, "自动检查收益是否过期开始。");
    	
    	couponTimeoutService.updateCouponTimeout();
    	
    	LogUtil.endLog(THIS_CLASS, methodName, "自动检查收益是否过期结束。");
    }

    
}
