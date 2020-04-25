package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.ProductStatisCustomize;

public interface ProductStatisCustomizeMapper {

	/**
	 * 获取汇天利统计记录
	 * @param ProductStatisCustomize
	 * @return
	 */
	List<ProductStatisCustomize> selectProductStatisRecord(ProductStatisCustomize productStatisCustomize);

	/**
	 * 获取汇天利出借人本金
	 * @param productStatisCustomize
	 * @return
	 */
	List<ProductStatisCustomize> selectUserProductPrincipal(ProductStatisCustomize productStatisCustomize);
	
	/**
	 * 汇天利新老客户分布  出借人数、转入金额
	 * @param ProductStatisCustomize
	 * @return
	 */
	List<ProductStatisCustomize> selectUserIsNewBuy(ProductStatisCustomize productStatisCustomize);
	/**
	 * 汇天利新老客户分布  出借人本金
	 * @param ProductStatisCustomize
	 * @return
	 */
	List<ProductStatisCustomize> selectUserIsNewPrincipal(ProductStatisCustomize productStatisCustomize);
	/**
	 * 汇天利新老客户分布  转出金额
	 * @param ProductStatisCustomize
	 * @return
	 */
	List<ProductStatisCustomize> selectUserIsNewRedeem(ProductStatisCustomize productStatisCustomize);

	
}