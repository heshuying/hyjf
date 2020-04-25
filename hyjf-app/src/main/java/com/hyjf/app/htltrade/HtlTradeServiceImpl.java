package com.hyjf.app.htltrade;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.ProductIntoRecordCustomize;
import com.hyjf.mybatis.model.customize.ProductRedeemCustomize;

@Service
public class HtlTradeServiceImpl extends BaseServiceImpl implements HtlTradeService {

	/**
	 * 获取汇天利转入记录数
	 * @param productIntoRecordCustomize
	 * @return
	 * @author Michael
	 */
	public Integer countHtlIntoRecord(ProductIntoRecordCustomize productIntoRecordCustomize) {
		return productIntoRecordCustomizeMapper.countHtlIntoRecord(productIntoRecordCustomize);
	}

	/**
	 * 获取汇天利转入记录列表
	 * 
	 * @return
	 */
	public List<ProductIntoRecordCustomize> getIntoRecordList(ProductIntoRecordCustomize productIntoRecordCustomize) {
		return productIntoRecordCustomizeMapper.selectHtlIntoRecord(productIntoRecordCustomize);
	}

	/**
	 * 获得转出记录数
	 * @param productRedeemCustomize
	 * @return
	 * @author Michael
	 */
	public Integer countProductRedeemRecord(ProductRedeemCustomize productRedeemCustomize) {
		return productRedeemCustomizeMapper.countProductRedeemRecord(productRedeemCustomize);
	}

	
	/**
	 * 获得转出记录列表
	 * @param ProductRedeemCustomize
	 * @return
	 */
	public List<ProductRedeemCustomize> getRedeemRecordList(ProductRedeemCustomize productRedeemCustomize) {
		return productRedeemCustomizeMapper.selectOutRecords(productRedeemCustomize);
	}

	
}
