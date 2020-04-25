package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.ProductExportIntoRecordCustomize;
import com.hyjf.mybatis.model.customize.ProductIntoRecordCustomize;

public interface ProductIntoRecordCustomizeMapper {

	/**
	 * 获取转入记录
	 */
	List<ProductIntoRecordCustomize> selectHtlIntoRecord(ProductIntoRecordCustomize productIntoRecordCustomize);

	/**
	 * 获得列表数
	 * 
	 * @param ProductIntoRecordCustomize
	 * @return
	 */
	Integer countHtlIntoRecord(ProductIntoRecordCustomize productIntoRecordCustomize);
	
	/**
	 * 导出报表
	 */
	List<ProductExportIntoRecordCustomize> exportHtlIntoRecord(ProductIntoRecordCustomize productIntoRecordCustomize);

	
	
}