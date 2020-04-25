package com.hyjf.admin.manager.plan;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanConfig;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;

public interface PlanService {

	/**
	 * 
	 * @method: getPlanTypeList
	 * @description: 计划类型查询
	 * @return
	 * @return: List<DebtPlanType>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	public List<DebtPlanConfig> getPlanTypeList();

	/**
	 * 计划列表查询件数
	 * 
	 * @method: countPlan
	 * @return: List<DebtPlanType>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	public int countPlan(PlanCommonCustomize planCommonCustomize);

	/**
	 * 计划列表查询
	 * 
	 * @method: selectPlanList
	 * @return: List<DebtPlanType>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	public List<DebtPlan> selectPlanList(PlanCommonCustomize planCommonCustomize);

	/**
	 * 
	 * @method: exportPlanList
	 * @description: 计划列表导出
	 * @return
	 * @return: List<DebtPlanType>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	public List<DebtPlan> exportPlanList(PlanCommonCustomize planCommonCustomize);

	/**
	 * 获取计划列表总计
	 * 
	 * @Title getPlanAccountTotal
	 * @param planCommonCustomize
	 * @return
	 */
	public Map<String, Object> getPlanAccountTotal(PlanCommonCustomize planCommonCustomize);
}
