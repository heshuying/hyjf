package com.hyjf.mybatis.mapper.customize.web;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.web.CouponUserListCustomize;

/**
 * 
 * 此处为类说明
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年6月12日
 * @see 下午6:54:41
 */
public interface CouponUserListCustomizeMapper {

	/**
	 * 获取优惠券列表
	 * 
	 * @param CouponConfigCustomize
	 * @return
	 */
	List<CouponUserListCustomize> selectCouponUserList(Map<String,Object> paraMap);
	
	
	/**
	 * 获取优惠券列表记录数
	 * 
	 * @param CouponConfigCustomize
	 * @return
	 */
	Integer countCouponUserList(Map<String,Object> paraMap);
	
	

}