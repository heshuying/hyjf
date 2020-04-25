package com.hyjf.app.htltrade;

import java.util.List;

import com.hyjf.mybatis.model.customize.ProductIntoRecordCustomize;
import com.hyjf.mybatis.model.customize.ProductRedeemCustomize;

public interface HtlTradeService {

	/**
	 * 获得购买列表数
	 * @param productIntoRecordCustomize
	 * @return
	 */
	public Integer countHtlIntoRecord(ProductIntoRecordCustomize productIntoRecordCustomize);
	

	/**
	 * 获取购买产品列表
	 * 
	 * @return
	 */
	public List<ProductIntoRecordCustomize> getIntoRecordList(ProductIntoRecordCustomize productIntoRecordCustomize);

	/**
	 * 获得汇天利转出列表数
	 * @param productIntoRecordCustomize
	 * @return
	 */
	public Integer countProductRedeemRecord(ProductRedeemCustomize productRedeemCustomize);
	/**
	 * 获取汇天利转出记录列表(自定义)
	 * @param ProductRedeemCustomize
	 * @return
	 */
	public List<ProductRedeemCustomize> getRedeemRecordList(ProductRedeemCustomize productRedeemCustomize);
}
