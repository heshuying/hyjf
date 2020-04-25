package com.hyjf.mybatis.mapper.customize;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.PlanStatisCustomize;

public interface PlanStatisCustomizeMapper {

	/**
	 * 获取汇天利统计记录
	 * @param PlanStatisCustomize
	 * @return
	 */
	List<PlanStatisCustomize> selectProductStatisRecord(PlanStatisCustomize PlanStatisCustomize);

	/**
	 * 获取汇天利出借人本金
	 * @param PlanStatisCustomize
	 * @return
	 */
	List<PlanStatisCustomize> selectUserProductPrincipal(PlanStatisCustomize PlanStatisCustomize);
	
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
	 * @method: countPlanStatic
	 * @description: 			
	 *  @param planStatisCustomize
	 *  @return 
	 * @return: Long
	* @mender: zhouxiaoshuai
	 * @date:   2016年11月17日 下午4:51:58
	 */
	Long countPlanStatic(PlanStatisCustomize planStatisCustomize);
/**
 * 
 * @method: getAccedeInfo
 * @description: 		查询加入金额分布	
 *  @param planStatisCustomize
 *  @return 
 * @return: List<Map<String,Object>>
* @mender: zhouxiaoshuai
 * @date:   2016年12月15日 上午10:46:54
 */
	List<Map<String, Object>> getAccedeInfo(PlanStatisCustomize planStatisCustomize);

	
}