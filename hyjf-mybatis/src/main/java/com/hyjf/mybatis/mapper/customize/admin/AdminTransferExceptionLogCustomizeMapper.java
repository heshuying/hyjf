package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.AdminTransferExceptionLogCustomize;

public interface AdminTransferExceptionLogCustomizeMapper {

	/**
	 * 获取优惠券列表
	 * 
	 * @param CouponConfigCustomize
	 * @return
	 */
	List<AdminTransferExceptionLogCustomize> selectTransferExceptionList(Map<String, Object> paraMap);
	
	/**
	 * 获取优惠券列表记录数
	 * 
	 * @param CouponConfigCustomize
	 * @return
	 */
	Integer countTransferException(Map<String, Object> paraMap);
	
	/**
	 * 取得优惠券编号
	 * @param paraMap
	 * @return
	 */
	String getCouponUserCode(Map<String, Object> paraMap);
}