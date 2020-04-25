package com.hyjf.admin.htl.productinterest;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.ProductInterestCustomize;

@Service
public class ProductInterestServiceImpl extends BaseServiceImpl implements ProductInterestService {


	
	/**
	 * 获得转出记录列表
	 * @param ProductInterestCustomize
	 * @return
	 */
	public List<ProductInterestCustomize> getRecordList(ProductInterestCustomize productInterestCustomize) {
		return productInterestCustomizeMapper.selectInterestRecords(productInterestCustomize);
	}

	/**
	 * 获得记录数
	 * @param productInterestCustomize
	 * @return
	 * @author Michael
	 */
	public Integer countRecord(ProductInterestCustomize productInterestCustomize) {
		return productInterestCustomizeMapper.countInterestRecord(productInterestCustomize);
			
	}
	

}
