package com.hyjf.admin.htl.productinterest;
import java.util.List;

import com.hyjf.mybatis.model.customize.ProductInterestCustomize;

public interface ProductInterestService {

	/**
	 * 获取转出记录列表(自定义)
	 * @param ProductInterestCustomize
	 * @return
	 */
	public List<ProductInterestCustomize> getRecordList(ProductInterestCustomize productInterestCustomize);

	
	/**
	 * 获得列表数
	 * @param ProductInterestCustomize
	 * @return
	 */
	public Integer countRecord(ProductInterestCustomize productInterestCustomize);

}
