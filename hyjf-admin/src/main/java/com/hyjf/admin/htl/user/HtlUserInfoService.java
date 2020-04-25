package com.hyjf.admin.htl.user;

import java.util.List;

import com.hyjf.mybatis.model.auto.Product;
import com.hyjf.mybatis.model.customize.HtlUserInfoCustomize;

public interface HtlUserInfoService {

	/**
	 * 获取用户信息列表
	 * 
	 * @return
	 */
	public List<HtlUserInfoCustomize> getRecordList(HtlUserInfoCustomize product);

	/**
	 * 获得列表数
	 * @param productIntoRecordCustomize
	 * @return
	 */
	public Integer countRecord(HtlUserInfoCustomize htlUserInfoCustomize);
	
	/**
	 * 获得汇天利产品信息
	 * @return
	 */

	public Product getProduct();

}
