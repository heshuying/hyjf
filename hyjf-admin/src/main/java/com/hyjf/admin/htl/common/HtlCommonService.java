package com.hyjf.admin.htl.common;

import java.util.List;

import com.hyjf.admin.htl.productinfo.ProductInfoBean;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.Product;
import com.hyjf.mybatis.model.auto.ProductInfo;

public interface HtlCommonService {

	
    /**
     * 获取数据字典表的下拉列表
     * 
     * @return
     */
    public List<ParamName> getParamNameList(String nameClass);

	/**
	 * 获取统计数据
	 * @param productInfo
	 * @return
	 */
    public List<ProductInfo> getProductInfoRecords(ProductInfoBean productInfo);
    
    
	/**
	 * 获取汇天利产品信息
	 * @return
	 */
	public Product getProduct();

}
