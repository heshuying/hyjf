package com.hyjf.admin.htl.productinto;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.ProductExportIntoRecordCustomize;
import com.hyjf.mybatis.model.customize.ProductIntoRecordCustomize;

@Service
public class ProductIntoRecordCustomizeServiceImpl extends BaseServiceImpl implements ProductIntoRecordCustomizeService {

	/**
	 * 获取汇天利转入记录列表
	 * 
	 * @return
	 */
	public List<ProductIntoRecordCustomize> getRecordList(ProductIntoRecordCustomize productIntoRecordCustomize) {
		return productIntoRecordCustomizeMapper.selectHtlIntoRecord(productIntoRecordCustomize);
	}

	/**
	 * 获得列表记录数
	 * @param productIntoRecordCustomize
	 * @return
	 * @author Michael
	 */
	public Integer countHtlIntoRecord(ProductIntoRecordCustomize productIntoRecordCustomize) {
		return productIntoRecordCustomizeMapper.countHtlIntoRecord(productIntoRecordCustomize);
	}

	/**
	 * 导出报表
	 * @param productIntoRecordCustomize
	 * @return
	 * @author Michael
	 */
	public List<ProductExportIntoRecordCustomize> exportExcel(ProductIntoRecordCustomize productIntoRecordCustomize) {
		return productIntoRecordCustomizeMapper.exportHtlIntoRecord(productIntoRecordCustomize);
	}
}
