package com.hyjf.admin.manager.plan.statis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.ProductList;
import com.hyjf.mybatis.model.auto.ProductListExample;
import com.hyjf.mybatis.model.auto.ProductRedeem;
import com.hyjf.mybatis.model.auto.ProductRedeemExample;
import com.hyjf.mybatis.model.customize.PlanStatisCustomize;

@Service
public class PlanStatisServiceImpl extends BaseServiceImpl implements PlanStatisService {

	/**
	 * 获取记录
	 * @param PlanStatisCustomize
	 * @return
	 * @author Michael
	 */
	public List<PlanStatisCustomize> getRecordList(PlanStatisCustomize PlanStatisCustomize) {
		return this.planStatisCustomizeMapper.selectProductStatisRecord(PlanStatisCustomize);
	}

	/**
	 * 获取汇天利出借人本金
	 * @param PlanStatisCustomize
	 * @return
	 */
	public List<PlanStatisCustomize> selectUserProductPrincipal(PlanStatisCustomize PlanStatisCustomize) {
		return this.planStatisCustomizeMapper.selectUserProductPrincipal(PlanStatisCustomize);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param PlanStatisCustomize
	 * @return
	 * @author Michael
	 */
		
	public List<ProductList> selectUserProductBuyRecord(PlanStatisCustomize PlanStatisCustomize) {
		ProductListExample productListExample = new ProductListExample();
		ProductListExample.Criteria cra = productListExample.createCriteria();
		cra.andInvestStatusEqualTo(0);
		return productListMapper.selectByExample(productListExample);
			
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param PlanStatisCustomize
	 * @return
	 * @author Michael
	 */
		
	public  List<ProductRedeem> selectUserProductRedeemRecord(PlanStatisCustomize PlanStatisCustomize) {
		ProductRedeemExample productRedeemExample = new ProductRedeemExample();
		ProductRedeemExample.Criteria cra = productRedeemExample.createCriteria();
		cra.andStatusEqualTo(0);
		return this.productRedeemMapper.selectByExample(productRedeemExample);
		
	}

	/**
	 * 汇天利新老客户分布  出借人数、转入金额
	 * @param PlanStatisCustomize
	 * @return
	 */
	public List<PlanStatisCustomize> selectUserIsNewBuy(PlanStatisCustomize PlanStatisCustomize) {
		return this.planStatisCustomizeMapper.selectUserIsNewBuy(PlanStatisCustomize);
	}

	/**
	 * 汇天利新老客户分布  出借人本金
	 * @param PlanStatisCustomize
	 * @return
	 */
		
	public List<PlanStatisCustomize> selectUserIsNewPrincipal(PlanStatisCustomize PlanStatisCustomize) {
		return this.planStatisCustomizeMapper.selectUserIsNewPrincipal(PlanStatisCustomize);
	}

	/**
	 * 汇天利新老客户分布  转出金额
	 * @param PlanStatisCustomize
	 * @return
	 */
	public List<PlanStatisCustomize> selectUserIsNewRedeem(PlanStatisCustomize PlanStatisCustomize) {
		return this.planStatisCustomizeMapper.selectUserIsNewRedeem(PlanStatisCustomize);
	}

	@Override
	public List<Map<String, Object>> getAccedeInfo(PlanStatisCustomize planStatisCustomize) {
		return this.planStatisCustomizeMapper.getAccedeInfo(planStatisCustomize);
	}


}
