package com.hyjf.batch.coupon.repay;

import java.util.List;

import com.hyjf.batch.BaseService;

public interface CouponRepayService extends BaseService {
	List<String> selectNidForCouponOnly();
}
