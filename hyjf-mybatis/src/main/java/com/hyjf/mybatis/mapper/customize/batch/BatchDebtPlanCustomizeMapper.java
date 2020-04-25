package com.hyjf.mybatis.mapper.customize.batch;

import com.hyjf.mybatis.model.auto.DebtPlan;

public interface BatchDebtPlanCustomizeMapper {

	/**
	 * 承接时修改更新计划表
	 * 
	 * @param debtPlan
	 * @return
	 */
	int updateDebtPlanAssign(DebtPlan debtPlan);

	/**
	 * 出借时更新计划表
	 * 
	 * @param debtPlan
	 * @return
	 */
	int updateDebtPlanInvest(DebtPlan debtPlan);

	/**
	 * 承接时，修改清算的计划
	 * @param sellerDebtPlan
	 * @return
	 */
	int updateDebtPlanSeller(DebtPlan sellerDebtPlan);
	
	/**
	 * 还款时,更新计划表
	 * @Title updatePlanRepay
	 * @param plan
	 * @return
	 */
	int updatePlanRepay(DebtPlan plan);
	

	/**
	 * 债转还款时,更新计划表
	 * @Title updatePlanCredit
	 * @param plan
	 * @return
	 */
	int updatePlanCredit(DebtPlan plan);


}