package com.hyjf.admin.htl.productredeemdetail;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.ProductRedeemDetailCustomize;

@Service
public class ProductRedeemDetailServiceImpl extends BaseServiceImpl implements ProductRedeemDetailService {


	
	/**
	 * 获得转出记录列表
	 * @param ProductRedeemCustomize
	 * @return
	 */
	public List<ProductRedeemDetailCustomize> getRecordList(ProductRedeemDetailCustomize productRedeemDetailCustomize) {
		return productRedeemDetailCustomizeMapper.selectRedeemDetailRecords(productRedeemDetailCustomize);
	}

	/**
	 * 获得记录条数
	 * @param productRedeemCustomize
	 * @return
	 * @author Michael
	 */
	public Integer countRedeemDetailRecord(ProductRedeemDetailCustomize productRedeemDetailCustomize) {
		return productRedeemDetailCustomizeMapper.countRedeemDetailRecord(productRedeemDetailCustomize);
			
	}
		

}
