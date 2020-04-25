package com.hyjf.bank.service.coupon;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseService;

public interface UserCouponService extends BaseService {
    /**
     * 
     * 自动发放用户优惠券
     * @author pcc
     * @param paramBean
     * @throws Exception 
     */
	JSONObject insertUserCoupon(UserCouponBean paramBean) throws Exception;
	

    
    /**
     * 
     * 批量自动发放用户优惠券
     * 
     * @author xj
     * @param paramBean
     * @throws Exception
     */
   
    JSONObject batchInsertUserCoupon(BatchUserCouponBean paramBean) throws Exception;

	
}
