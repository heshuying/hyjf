package com.hyjf.admin.htl.user;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.Product;
import com.hyjf.mybatis.model.auto.ProductExample;
import com.hyjf.mybatis.model.customize.HtlUserInfoCustomize;

@Service
public class HtlUserInfoServiceImpl extends BaseServiceImpl implements HtlUserInfoService {

	/**
	 * 获取用户信息列表
	 * 
	 * @return
	 */
	public List<HtlUserInfoCustomize> getRecordList(HtlUserInfoCustomize HtlUserInfo) {
		return htlUserInfoCustomizeMapper.selectHtlUserInfos(HtlUserInfo);
	}

	/**
	 * 获得列表条数
	 * @param htlUserInfoCustomize
	 * @return
	 * @author Michael
	 */
	public Integer countRecord(HtlUserInfoCustomize htlUserInfoCustomize) {
		return htlUserInfoCustomizeMapper.countHtlUserInfoRecord(htlUserInfoCustomize);
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
