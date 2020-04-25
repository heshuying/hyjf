package com.hyjf.admin.htl.common;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.htl.productinfo.ProductInfoBean;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.ParamNameExample;
import com.hyjf.mybatis.model.auto.Product;
import com.hyjf.mybatis.model.auto.ProductExample;
import com.hyjf.mybatis.model.auto.ProductInfo;
import com.hyjf.mybatis.model.auto.ProductInfoExample;

@Service
public class HtlCommonServiceImpl extends BaseServiceImpl implements HtlCommonService {

    /**
     * 获取数据字典表的下拉列表
     * 
     * @return
     */
    public List<ParamName> getParamNameList(String nameClass) {
        ParamNameExample example = new ParamNameExample();
        ParamNameExample.Criteria cra = example.createCriteria();
        cra.andNameClassEqualTo(nameClass);
        cra.andDelFlagEqualTo(CustomConstants.FLAG_NORMAL);
        example.setOrderByClause(" sort ASC ");
        return this.paramNameMapper.selectByExample(example);
    }

	/**
	 * 获取统计数据
	 * @param productInfo
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<ProductInfo> getProductInfoRecords(ProductInfoBean productInfo) {
		ProductInfoExample productInfoExample = new ProductInfoExample();
		return this.productInfoMapper.selectByExample(productInfoExample);
			
	}

	/**
	 * 获得汇天利产品信息
	 * @return
	 */
	public Product getProduct(){
		Product product = new Product();
		List<Product> productinfoList = this.productMapper.selectByExample(new ProductExample());
		if(productinfoList.size()>0){
			product = productinfoList.get(0);
		}
		return product;
	}

}
