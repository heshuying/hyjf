package com.hyjf.mybatis.mapper.customize;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.customize.DebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;
import com.hyjf.mybatis.model.customize.PlanCustomize;

public interface PlanCustomizeMapper {
	/**
	 * 
	 * @method: getPlanTypeList
	 * @description: 计划数量查询
	 * @return
	 * @return: List<DebtPlanType>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	Long countPlan(PlanCommonCustomize planCommonCustomize);

	/**
	 * 
	 * @method: selectPlanList
	 * @description: 计划列表查询
	 * @return
	 * @return: List<DebtPlanType>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	List<PlanCustomize> selectPlanList(PlanCommonCustomize planCommonCustomize);

	/**
	 * 
	 * @method: exportPlanList
	 * @description: 计划列表导出
	 * @return
	 * @return: List<DebtPlanType>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	List<PlanCommonCustomize> exportPlanList(PlanCommonCustomize planCommonCustomize);

	/**
	 * 获取计划预编号
	 * 
	 * @Title getPlanPreNid
	 * @param planPreNid
	 * @return
	 */
	public String getPlanPreNid(@Param("mmdd") String mmdd);

	/**
	 * 获取计划关联资产信息
	 * 
	 * @Title getDebtPlanBorrowList
	 * @param param
	 * @return
	 */
	public List<DebtPlanBorrowCustomize> getDebtPlanBorrowList(Map<String, Object> param);

	/**
	 * 获取计划的关联资产的件数
	 * 
	 * @Title countDebtPlanBorrowList
	 * @param param
	 * @return
	 */
	public int countDebtPlanBorrowList(Map<String, Object> param);

	/**
	 * 根据汇添金专属标号查询已关联的计划编号
	 * 
	 * @Title getDebtPlanNidListByBorrowNid
	 * @param param
	 * @return
	 */
	public List<String> getDebtPlanNidListByBorrowNid(Map<String, Object> param);

	/**
	 * 根据计划编号,专属标号查询该计划是否已经关联资产
	 * 
	 * @Title getPlanIsSelected
	 * @param param
	 * @return
	 */
	public String getPlanIsSelected(Map<String, Object> param);

	/**
	 * 根据计划编号检索计划已经关联的汇添金资产
	 * 
	 * @Title selectDebtPlanBorrowListByDebtPlanNid
	 * @param param
	 * @return
	 */
	public List<DebtPlanBorrowCustomize> selectDebtPlanBorrowListByDebtPlanNid(Map<String, Object> param);

	/**
	 * 获取计划编号检索计划关联的资产配置件数
	 * 
	 * @Title countDebtPlanBorrowListByDebtPlanNid
	 * @param param
	 * @return
	 */
	public int countDebtPlanBorrowListByDebtPlanNid(Map<String, Object> param);

	/**
	 * 获取关联资产总计
	 * 
	 * @Title countDebtPlanAmount
	 * @param param
	 * @return
	 */
	public Map<String, Object> countDebtPlanAmount(Map<String, Object> param);
	
	/**
	 * 获取计划金额总计
	 * @Title getPlanAccountTotal
	 * @param planCommonCustomize
	 * @return
	 */
	public Map<String, Object> getPlanAccountTotal(PlanCommonCustomize planCommonCustomize);
	
	
	/**
	 * 获取已关联资产总计
	 * @Title countDebtPlanBorrowListAmount
	 * @param param
	 * @return
	 */
	public Map<String,Object> countDebtPlanBorrowListAmount(Map<String, Object> param);
}