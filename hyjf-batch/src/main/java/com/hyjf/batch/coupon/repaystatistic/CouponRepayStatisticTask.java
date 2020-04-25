package com.hyjf.batch.coupon.repaystatistic;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.log.LogUtil;

public class CouponRepayStatisticTask {

	public static final String THIS_CLASS = CouponRepayStatisticTask.class.getName();

	@Autowired
	CouponRepayStatisticService couponRepayStatisticService;

	public void run() {
		updateCouponRepayStatistic();
	}

	public void updateCouponRepayStatistic() {
		LogUtil.startLog(THIS_CLASS, "updateCouponRepayStatistic", "统计加息券收益");
		couponRepayStatisticService.updateOrSaveCouponStatistic();
		LogUtil.endLog(THIS_CLASS, "updateCouponRepayStatistic", "统计加息券收益");
	}
}
