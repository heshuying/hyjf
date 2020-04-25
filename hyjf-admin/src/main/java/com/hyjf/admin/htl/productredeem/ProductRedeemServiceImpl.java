package com.hyjf.admin.htl.productredeem;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.ProductExportOutRecordCustomize;
import com.hyjf.mybatis.model.customize.ProductRedeemCustomize;

@Service
public class ProductRedeemServiceImpl extends BaseServiceImpl implements ProductRedeemService {


	
	/**
	 * 获得转出记录列表
	 * @param ProductRedeemCustomize
	 * @return
	 */
	public List<ProductRedeemCustomize> getRecordList(ProductRedeemCustomize productRedeemCustomize) {
		return productRedeemCustomizeMapper.selectOutRecords(productRedeemCustomize);
	}

	/**
	 * 获得记录数
	 * @param productRedeemCustomize
	 * @return
	 * @author Michael
	 */
	public Integer countProductRedeemRecord(ProductRedeemCustomize productRedeemCustomize) {
		return productRedeemCustomizeMapper.countProductRedeemRecord(productRedeemCustomize);
	}

	/**
	 * 报表导出
	 * @param productRedeemCustomize
	 * @return
	 * @author Michael
	 */
	public List<ProductExportOutRecordCustomize> exportExcel(ProductRedeemCustomize productRedeemCustomize) {
		return productRedeemCustomizeMapper.exportOutRecords(productRedeemCustomize);
	}
	

}
