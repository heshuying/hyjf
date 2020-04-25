package com.hyjf.admin.htl.productinto;

import java.util.List;

import com.hyjf.mybatis.model.customize.ProductExportIntoRecordCustomize;
import com.hyjf.mybatis.model.customize.ProductIntoRecordCustomize;

public interface ProductIntoRecordCustomizeService {

	/**
	 * 获取产品列表
	 * 
	 * @return
	 */
	public List<ProductIntoRecordCustomize> getRecordList(ProductIntoRecordCustomize productIntoRecordCustomize);

	/**
	 * 获得列表数
	 * @param productIntoRecordCustomize
	 * @return
	 */
	public Integer countHtlIntoRecord(ProductIntoRecordCustomize productIntoRecordCustomize);
	
	/**
	 * 获取产品列表(报表导出)
	 * 
	 * @return
	 */
	public List<ProductExportIntoRecordCustomize> exportExcel(ProductIntoRecordCustomize productIntoRecordCustomize);

}
