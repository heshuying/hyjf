package com.hyjf.admin.manager.plan.statis;
import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.ProductList;
import com.hyjf.mybatis.model.auto.ProductRedeem;
import com.hyjf.mybatis.model.customize.PlanStatisCustomize;

public interface PlanStatisService {

	/**
	 * 获取统计信息
	 * @param ProductRedeemCustomize
	 * @return
	 */
	public List<PlanStatisCustomize> getRecordList(PlanStatisCustomize PlanStatisCustomize);

	/**
	 * 获取汇天利出借人本金
	 * @param PlanStatisCustomize
	 * @return
	 */
	List<PlanStatisCustomize> selectUserProductPrincipal(PlanStatisCustomize PlanStatisCustomize);
	
	/**
	 * 获取汇天利转入记录
	 * @param PlanStatisCustomize
	 * @return
	 */
	List<ProductList> selectUserProductBuyRecord(PlanStatisCustomize PlanStatisCustomize);
	
	/**
	 * 获取汇天利转出记录
	 * @param PlanStatisCustomize
	 * @return
	 */
	List<ProductRedeem> selectUserProductRedeemRecord(PlanStatisCustomize PlanStatisCustomize);
	
	
	/**
	 * 汇天利新老客户分布  出借人数、转入金额
	 * @param PlanStatisCustomize
	 * @return
	 */
	List<PlanStatisCustomize> selectUserIsNewBuy(PlanStatisCustomize PlanStatisCustomize);
	/**
	 * 汇天利新老客户分布  出借人本金
	 * @param PlanStatisCustomize
	 * @return
	 */
	List<PlanStatisCustomize> selectUserIsNewPrincipal(PlanStatisCustomize PlanStatisCustomize);
	/**
	 * 汇天利新老客户分布  转出金额
	 * @param PlanStatisCustomize
	 * @return
	 */
	List<PlanStatisCustomize> selectUserIsNewRedeem(PlanStatisCustomize PlanStatisCustomize);
/**
 * 
 * @method: getAccedeInfo
 * @description: 		三期获取用户加入数据	
 *  @param planStatisCustomize
 *  @return 
 * @return: List<Map<String,Object>>
* @mender: zhouxiaoshuai
 * @date:   2016年12月14日 下午4:35:55
 */
	public List<Map<String, Object>> getAccedeInfo(PlanStatisCustomize planStatisCustomize);

}
