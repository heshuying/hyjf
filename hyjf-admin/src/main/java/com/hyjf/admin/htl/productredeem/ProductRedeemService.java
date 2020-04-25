package com.hyjf.admin.htl.productredeem;
import java.util.List;

import com.hyjf.mybatis.model.customize.ProductExportOutRecordCustomize;
import com.hyjf.mybatis.model.customize.ProductRedeemCustomize;

public interface ProductRedeemService {

	/**
	 * 获取转出记录列表(自定义)
	 * @param ProductRedeemCustomize
	 * @return
	 */
	public List<ProductRedeemCustomize> getRecordList(ProductRedeemCustomize productRedeemCustomize);

	
	/**
	 * 获得列表数
	 * @param productIntoRecordCustomize
	 * @return
	 */
	public Integer countProductRedeemRecord(ProductRedeemCustomize productRedeemCustomize);
	
	/**
	 * 报表导出
	 * @param ProductRedeemCustomize
	 * @return
	 */
	public List<ProductExportOutRecordCustomize> exportExcel(ProductRedeemCustomize productRedeemCustomize);

	
}
