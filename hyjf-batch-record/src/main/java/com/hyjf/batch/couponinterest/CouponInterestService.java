package com.hyjf.batch.couponinterest;

import java.util.List;
import java.util.Map;

public interface CouponInterestService {

	List<Map<String, Object>> getDataForUpdate();

	void updateCouponUserAccount(List<Map<String, Object>> dataList) throws Exception;

}
