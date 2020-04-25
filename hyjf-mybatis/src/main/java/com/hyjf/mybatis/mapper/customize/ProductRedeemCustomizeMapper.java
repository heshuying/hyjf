package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.ProductExportOutRecordCustomize;
import com.hyjf.mybatis.model.customize.ProductRedeemCustomize;

public interface ProductRedeemCustomizeMapper {

	/**
	 * 获取汇天利转出记录
	 * @param ProductRedeemCustomize
	 * @return
	 */
	List<ProductRedeemCustomize> selectOutRecords(ProductRedeemCustomize productRedeemCustomize);


	/**
	 * 获得列表数
	 * 
	 * @param ProductRedeemCustomize
	 * @return
	 */
	Integer countProductRedeemRecord(ProductRedeemCustomize productRedeemCustomize);
	
	/**
	 * 转出记录(报表导出)
	 * @param ProductRedeemCustomize
	 * @return
	 */
	List<ProductExportOutRecordCustomize> exportOutRecords(ProductRedeemCustomize productRedeemCustomize);

}