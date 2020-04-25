package com.hyjf.admin.htl.statis;
import java.util.List;

import com.hyjf.mybatis.model.auto.ProductList;
import com.hyjf.mybatis.model.auto.ProductRedeem;
import com.hyjf.mybatis.model.customize.ProductStatisCustomize;

public interface ProductStatisService {

	/**
	 * 获取统计信息
	 * @param ProductRedeemCustomize
	 * @return
	 */
	public List<ProductStatisCustomize> getRecordList(ProductStatisCustomize productStatisCustomize);

	/**
	 * 获取汇天利出借人本金
	 * @param productStatisCustomize
	 * @return
	 */
	List<ProductStatisCustomize> selectUserProductPrincipal(ProductStatisCustomize productStatisCustomize);
	
	/**
	 * 获取汇天利转入记录
	 * @param productStatisCustomize
	 * @return
	 */
	List<ProductList> selectUserProductBuyRecord(ProductStatisCustomize productStatisCustomize);
	
	/**
	 * 获取汇天利转出记录
	 * @param productStatisCustomize
	 * @return
	 */
	List<ProductRedeem> selectUserProductRedeemRecord(ProductStatisCustomize productStatisCustomize);
	
	
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
