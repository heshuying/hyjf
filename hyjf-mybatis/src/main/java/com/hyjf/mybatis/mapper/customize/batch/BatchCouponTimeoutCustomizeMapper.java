package com.hyjf.mybatis.mapper.customize.batch;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.batch.BatchCouponTimeoutCommonCustomize;

public interface BatchCouponTimeoutCustomizeMapper {

	/**
	 * 根据用户编号及优惠券的过期时间，取得待过期的体验金金额
	 * 
	 * @return
	 */
	List<BatchCouponTimeoutCommonCustomize> selectCouponQuota(Map<String,Object> paramMap);

}