package com.hyjf.mybatis.mapper.customize.coupon;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.CouponRecover;
import com.hyjf.mybatis.model.customize.coupon.CouponRecoverCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderCustomize;

public interface CouponRecoverCustomizeMapper {

	/**
	 * 获取优惠券出借还款列表
	 * 
	 * @param CouponConfigCustomize
	 * @return
	 */
	CouponRecoverCustomize selectCurrentCouponRecover(Map<String,Object> paramMap);
	
	/**
	 * 
	 * 获取优惠券出借还款列表(还款失败)
	 * @author hsy
	 * @param paramMap
	 * @return
	 */
	CouponRecoverCustomize selectCurrentCouponRecoverFailed(Map<String,Object> paramMap);
	
	/**
	 * 取得未领取的体验金收益
	 * @param paramMap
	 * @return
	 */
	List<CouponRecoverCustomize> selectCouponRecoverWait(Map<String,Object> paramMap);
	

	/**
	 * 取得未领取的体验金收益
	 * @param paramMap
	 * @return
	 */
	List<CouponRecover> selectCrWaitPnr();
	
	/**
	 * 取得标的下的优惠券出借列表
	 * @param paramMap
	 * @return
	 */
	List<CouponTenderCustomize> selectCouponRecoverAll(Map<String,Object> paramMap);
	

	/**
	 * 
	 * 获取某用户优惠券待收收益总和
	 * @param userId
	 * @return
	 */
	String selectCouponInterestTotal(Integer userId);
	
	/**
	 * 
	 * 获取某用户优惠券累计收益总和
	 * @author hsy
	 * @param userId
	 * @return
	 */
	String selectCouponReceivedInterestTotal(Integer userId);
	
	/**
	 * 更新还款期状态
	 * @param tenderNid
	 */
	void crRecoverPeriod(Map<String,Object> paramMap);
	
	/**
	 * 
	 * 统计今日待还款收益总和
	 * @author hsy
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> selectCouponInterestWaitToday(Map<String,Object> paramMap);
	
	   /**
     * 
     * 统计今日已还款收益总和
     * @author hsy
     * @param paramMap
     * @return
     */
    BigDecimal selectCouponInterestReceivedToday(Map<String,Object> paramMap);
    
    /**
     * 
     * 取得汇计划的优惠券出借列表
     * @author hsy
     * @param paramMap
     * @return
     */
    List<CouponTenderCustomize> selectCouponRecoverHjh(Map<String,Object> paramMap);
	
}