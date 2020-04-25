package com.hyjf.batch.htl;

import com.hyjf.mybatis.model.auto.ProductInfo;
import com.hyjf.mybatis.model.customize.ProductInfoCustomize;

public interface HtlCommonService {

	/**
	 * sql 生成数据（定时插入product_info）
	 * @return
	 */
	public ProductInfo getCreateProductInfo(ProductInfoCustomize productInfoCustomize);

}
