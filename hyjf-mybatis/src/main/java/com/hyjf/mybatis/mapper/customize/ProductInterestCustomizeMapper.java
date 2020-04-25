package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.ProductInterestCustomize;

public interface ProductInterestCustomizeMapper {

	/**
	 * 获取汇天利利息记录
	 * @param ProductRedeemCustomize
	 * @return
	 */
	List<ProductInterestCustomize> selectInterestRecords(ProductInterestCustomize productInterestCustomize);

	/**
	 * 获得列表数
	 * 
	 * @param ProductInterestCustomize
	 * @return
	 */
	Integer countInterestRecord(ProductInterestCustomize productInterestCustomize);
	
}