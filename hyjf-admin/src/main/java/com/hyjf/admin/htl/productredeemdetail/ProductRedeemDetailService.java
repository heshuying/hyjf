package com.hyjf.admin.htl.productredeemdetail;
import java.util.List;

import com.hyjf.mybatis.model.customize.ProductRedeemDetailCustomize;

public interface ProductRedeemDetailService {

	/**
	 * 获取转出记录详细列表
	 * @param ProductRedeemCustomize
	 * @return
	 */
	public List<ProductRedeemDetailCustomize> getRecordList(ProductRedeemDetailCustomize productRedeemDetailCustomize);

	/**
	 * 获得列表数
	 * @param ProductRedeemDetailCustomize
	 * @return
	 */
	public Integer countRedeemDetailRecord(ProductRedeemDetailCustomize productRedeemCustomize);
}
