package com.hyjf.mybatis.mapper.customize.coupon;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.app.AppCouponInfoCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigExoportCustomize;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;

public interface CouponConfigCustomizeMapper {

	/**
	 * 获取优惠券列表
	 * 
	 * @param CouponConfigCustomize
	 * @return
	 */
	List<CouponConfigCustomize> selectCouponConfigList(CouponConfigCustomize couponConfigCustomize);
	/**
	 * 获取优惠券列表记录数
	 * 
	 * @param CouponConfigCustomize
	 * @return
	 */
	Integer countCouponConfig(CouponConfigCustomize couponConfigCustomize);
	
	/**
	 * 导出优惠券列表
	 * 
	 * @param CouponConfigCustomize
	 * @return
	 */
	List<CouponConfigExoportCustomize> exportCouponConfigList(CouponConfigCustomize couponConfigCustomize);
	/**
	 * 
	 * 获取用户优惠券列表
	 * @author pcc
	 * @param map
	 * @return
	 */
    List<UserCouponConfigCustomize> getCouponConfigList(Map<String, Object> map);
    /**
     * 获取用户优惠券id获得优惠券信息
     * @author pcc
     * @param couponId
     * @return
     */
    UserCouponConfigCustomize getBestCouponById(String couponId);
    
    /**
     * 
     * 获取优惠券已发行数量
     * @author hsy
     * @param couponCode
     * @return
     */
    int selectCouponPublishedCount(String couponCode);
    
    /**
     * 
     * 校验优惠券发放数量是否超限
     * @author hsy
     * @param param
     * @return
     */
    int checkCouponSendExcess(Map<String,Object> param);
    
    /**
	 * 根据用户ID和borrowNid查询该项目所用的优惠券信息
	 * @param userId 用户ID
	 * @param borrowNid 
	 * @return
	 */
	AppCouponInfoCustomize getCouponfigByUserIdAndBorrowNid(Map<String,Object> param);
}