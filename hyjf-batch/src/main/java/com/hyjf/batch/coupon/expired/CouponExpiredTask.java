package com.hyjf.batch.coupon.expired;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.log.LogUtil;

/**
 * 优惠券过期未使用
 * 
 * @author Administrator
 * 
 */
public class CouponExpiredTask {
	/** 类名 */
	private static final String THIS_CLASS = CouponExpiredTask.class.getName();
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	@Autowired
	CouponExpiredService couponExpiredService;

	public void run() {
		if (isOver) {
			try {
				isOver = false;
				//
				updateCouponExpired();
			} finally {
				isOver = true;
			}
		}
	}

	/**
	 * 检查优惠券使用是否过期
	 */
	private void updateCouponExpired() {
		String methodName = "updateCouponExpired";
		LogUtil.startLog(THIS_CLASS, methodName, "检查优惠券使用是否过期 开始");
		System.out.println("检查优惠券使用是否过期 -start");
		couponExpiredService.updateCouponExpired();
		System.out.println("检查优惠券使用是否过期 -end");
		LogUtil.endLog(THIS_CLASS, methodName, "检查优惠券使用是否过期 开始");
	}

}
