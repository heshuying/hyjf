package com.hyjf.admin.htl.statis;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.ProductList;
import com.hyjf.mybatis.model.auto.ProductListExample;
import com.hyjf.mybatis.model.auto.ProductRedeem;
import com.hyjf.mybatis.model.auto.ProductRedeemExample;
import com.hyjf.mybatis.model.customize.ProductStatisCustomize;

@Service
public class ProductStatisServiceImpl extends BaseServiceImpl implements ProductStatisService {

	/**
	 * 获取记录
	 * @param productStatisCustomize
	 * @return
	 * @author Michael
	 */
	public List<ProductStatisCustomize> getRecordList(ProductStatisCustomize productStatisCustomize) {
		return this.productStatisCustomizeMapper.selectProductStatisRecord(productStatisCustomize);
	}

	/**
	 * 获取汇天利出借人本金
	 * @param productStatisCustomize
	 * @return
	 */
	public List<ProductStatisCustomize> selectUserProductPrincipal(ProductStatisCustomize productStatisCustomize) {
		return this.productStatisCustomizeMapper.selectUserProductPrincipal(productStatisCustomize);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param productStatisCustomize
	 * @return
	 * @author Michael
	 */
		
	public List<ProductList> selectUserProductBuyRecord(ProductStatisCustomize productStatisCustomize) {
		ProductListExample productListExample = new ProductListExample();
		ProductListExample.Criteria cra = productListExample.createCriteria();
		cra.andInvestStatusEqualTo(0);
		if(productStatisCustomize.getTimeStart() != null){
			cra.andInvestTimeGreaterThanOrEqualTo(productStatisCustomize.getTimeStart());
		}
		if(productStatisCustomize.getTimeEnd() != null){
			cra.andInvestTimeLessThanOrEqualTo(productStatisCustomize.getTimeEnd());
		}
		return productListMapper.selectByExample(productListExample);
			
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param productStatisCustomize
	 * @return
	 * @author Michael
	 */
		
	public  List<ProductRedeem> selectUserProductRedeemRecord(ProductStatisCustomize productStatisCustomize) {
		ProductRedeemExample productRedeemExample = new ProductRedeemExample();
		ProductRedeemExample.Criteria cra = productRedeemExample.createCriteria();
		cra.andStatusEqualTo(0);
		if(productStatisCustomize.getTimeStart() != null ){
			cra.andRedeemTimeGreaterThanOrEqualTo(productStatisCustomize.getTimeStart());
		}
		if(productStatisCustomize.getTimeEnd() != null ){
			cra.andRedeemTimeLessThanOrEqualTo(productStatisCustomize.getTimeEnd());
		}
		return this.productRedeemMapper.selectByExample(productRedeemExample);
		
	}

	/**
	 * 汇天利新老客户分布  出借人数、转入金额
	 * @param ProductStatisCustomize
	 * @return
	 */
	public List<ProductStatisCustomize> selectUserIsNewBuy(ProductStatisCustomize productStatisCustomize) {
		return this.productStatisCustomizeMapper.selectUserIsNewBuy(productStatisCustomize);
	}

	/**
	 * 汇天利新老客户分布  出借人本金
	 * @param ProductStatisCustomize
	 * @return
	 */
		
	public List<ProductStatisCustomize> selectUserIsNewPrincipal(ProductStatisCustomize productStatisCustomize) {
		return this.productStatisCustomizeMapper.selectUserIsNewPrincipal(productStatisCustomize);
	}

	/**
	 * 汇天利新老客户分布  转出金额
	 * @param ProductStatisCustomize
	 * @return
	 */
	public List<ProductStatisCustomize> selectUserIsNewRedeem(ProductStatisCustomize productStatisCustomize) {
		return this.productStatisCustomizeMapper.selectUserIsNewRedeem(productStatisCustomize);
	}


}
