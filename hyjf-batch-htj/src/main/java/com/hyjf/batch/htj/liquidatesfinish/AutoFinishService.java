package com.hyjf.batch.htj.liquidatesfinish;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.DebtPlan;

public interface AutoFinishService extends BaseService {


	/**
	 * 查询相应的已清算的计划（liquidate_fact_time升序排列）
	 * 
	 * @param expectApr
	 * @return
	 */

	public List<DebtPlan> selectDebtPlanLiquidates();

	/**
	 * 查询相应的清算债权是否已经完成
	 * @param liquidatesPlanNid
	 * @return
	 */
	int countDebtCreditsAll(String liquidatesPlanNid);

	/**
	 * 发送短信
	 * @param debtPlanLiquidates
	 */
	void sendSms(DebtPlan debtPlanLiquidates);

	/**
	 * 发送email
	 * @param debtPlanLiquidates
	 */
	void sendEmail(DebtPlan debtPlanLiquidates);

	/**
	 * 更新为清算完成
	 * @param debtPlanLiquidates
	 * @return
	 * @throws Exception
	 */
	boolean updateDebtPlan(DebtPlan debtPlanLiquidates) throws Exception;

}
