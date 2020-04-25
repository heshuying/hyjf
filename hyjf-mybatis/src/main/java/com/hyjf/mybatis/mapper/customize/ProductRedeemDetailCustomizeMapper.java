package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.ProductRedeemDetailCustomize;

public interface ProductRedeemDetailCustomizeMapper {

	/**
	 * 获取汇天利转出记录明细
	 * @param ProductRedeemDetailCustomize
	 * @return
	 */
	List<ProductRedeemDetailCustomize> selectRedeemDetailRecords(ProductRedeemDetailCustomize productRedeemDetailCustomize);
	/**
	 * 根据转出订单id 查询出统计信息
	 * @param productRedeemDetailCustomize
	 * @return
	 */
	List<ProductRedeemDetailCustomize> selectRedeemListSumByListId(ProductRedeemDetailCustomize productRedeemDetailCustomize);

	/**
	 * 获得列表数
	 * 
	 * @param ProductRedeemDetailCustomize
	 * @return
	 */
	Integer countRedeemDetailRecord(ProductRedeemDetailCustomize productRedeemDetailCustomize);

	
	
}