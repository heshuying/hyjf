package com.hyjf.mybatis.mapper.customize.callcenter;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.callcenter.CallCenterCouponBackMoneyCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterCouponTenderCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterCouponUserCustomize;

public interface CallCenterCouponUserCustomizeMapper {

	/**
	 * 按照用户名/手机号查询优惠券
	 * 
	 * @param CouponConfigCustomize
	 * @return
	 */
	List<CallCenterCouponUserCustomize> selectCouponUserList(Map<String,Object> paraMap);
	
	/**
     * 按照用户名/手机号查询优惠券使用（直投产品）
     * 
     * @param CouponConfigCustomize
     * @return
     */
    List<CallCenterCouponTenderCustomize> selectCouponTenderList(Map<String,Object> paraMap);
    
    
    
    
    /**
     * 按照用户名/手机号查询优惠券回款（直投产品）
     * 
     * @param CouponConfigCustomize
     * @return
     */
    List<CallCenterCouponBackMoneyCustomize> selectCouponBackMoneyList(Map<String,Object> paraMap);
    
}