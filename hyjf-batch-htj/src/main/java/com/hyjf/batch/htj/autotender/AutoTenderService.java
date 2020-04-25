package com.hyjf.batch.htj.autotender;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;

public interface AutoTenderService extends BaseService {

	/**
	 * 按规则进行比例债权出借
	 * 
	 * @param debtPlanAccede
	 * @param debtPlan
	 * @return
	 */
	boolean ruleCreditAssign(DebtPlan debtPlan, DebtPlanAccede debtPlanAccede);

	/**
	 * 按规则进行出借
	 * 
	 * @param debtPlanAccede
	 * @param debtPlan
	 * @return
	 */
	boolean ruleDebtTender(DebtPlan debtPlan, DebtPlanAccede debtPlanAccede);

	/**
	 * 按规则进行比例债权出借
	 * 
	 * @param debtPlanAccede
	 * @param debtPlan
	 * @return
	 */
	boolean unableRuleCreditAssign(DebtPlan debtPlan, DebtPlanAccede debtPlanAccede);

	/**
	 * 按规则进行出借
	 * 
	 * @param debtPlanAccede
	 * @param debtPlan
	 * @return
	 */
	boolean unableRuleDebtTender(DebtPlan debtPlan, DebtPlanAccede debtPlanAccede);

	/**
	 * 复投承接债权
	 * 
	 * @param debtPlan
	 * @param debtPlanAccede
	 * @return
	 */
	boolean reinvestCreditAssign(DebtPlan debtPlan, DebtPlanAccede debtPlanAccede);

	/**
	 * 复投投标汇天金专属标的
	 * 
	 * @param debtPlan
	 * @param debtPlanAccede
	 * @return
	 */
	boolean reinvestDebtTender(DebtPlan debtPlan, DebtPlanAccede debtPlanAccede);

	/**
	 * 查询相应的募集中的计划（按buy_begin_time申购开始时间升序排列）
	 * 
	 * @param planStatus
	 * 
	 * @return
	 */

	public List<DebtPlan> selectDebtPlanInvest();

	/**
	 * 根据计划nid查询相应的计划加入记录
	 * 
	 * @param planNid
	 * @param cycleTimes
	 * @param investAccountMin
	 * @return
	 */

	public List<DebtPlanAccede> selectDebtPlanAccede(String planNid, BigDecimal investAccountMin);

	/**
	 * 查询相应的已清算的计划（liquidate_fact_time升序排列）
	 * 
	 * @param planNid
	 * 
	 * @param expectApr
	 * @return
	 */

	public List<DebtPlan> selectDebtPlanLiquidates(String planNid);

	/**
	 * 校验用户的授权状态
	 * 
	 * @param userId
	 * @param planOrderId
	 * @param planNid
	 * @param accedeBalance
	 * @return
	 * @throws Exception
	 */

	public boolean checkUserAuthInfo(int userId, BigDecimal accedeBalance, String planNid, String planOrderId) throws Exception;

	/**
	 * 更新用户的计划加入订单为完成
	 * 
	 * @param debtPlanAccede
	 * @return
	 */

	boolean updateDebtPlanAccedeFinish(DebtPlanAccede debtPlanAccede);

	/**
	 * 更新计划加入表的遍历次数
	 * 
	 * @param debtPlanAccede
	 * @return
	 */

	boolean updateDebtPlanAccede(DebtPlanAccede debtPlanAccede);

}
