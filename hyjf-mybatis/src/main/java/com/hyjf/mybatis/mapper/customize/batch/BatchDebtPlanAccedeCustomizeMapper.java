package com.hyjf.mybatis.mapper.customize.batch;

import java.math.BigDecimal;
import java.util.Map;

import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.customize.batch.BatchDebtPlanAccedeCustomize;

public interface BatchDebtPlanAccedeCustomizeMapper {

	/**
	 * 更新相应的计划加入余额
	 * @param debtPlanAccede
	 * @return
	 */
		
	int updatePlanAccedeInvest(DebtPlanAccede debtPlanAccede);

	/**
	 * 债权承接修改承接人计划加入表
	 * @param debtPlanAccedeNew
	 * @return
	 */
		
	int updateDebtPlanAccedeAssign(DebtPlanAccede debtPlanAccedeNew);
	
	/**
	 * 债权承接修改出让人计划加入表
	 * @param assignDebtPlanAccede
	 * @return
	 */
		
	int updateDebtPlanAccedeSeller(DebtPlanAccede assignDebtPlanAccede);
	
	
	/**
	 * 债权承接冻结修改出让人计划加入表
	 * @param assignDebtPlanAccede
	 * @return
	 */
		
	int updateDebtPlanAccedeSellerFreeze(DebtPlanAccede assignDebtPlanAccede);
	
	
	/**
	 * 查询协议内容
	 * 
	 * @Title selectPlanAccedeInfo
	 * @param params
	 * @return
	 */
	public BatchDebtPlanAccedeCustomize selectPlanAccedeInfo(Map<String, String> params);

	/**
	 * 根据相应的用户userId,加入订单对应的承接债权的待还金额
	 * @param params
	 * @return
	 */
		
	BigDecimal selectDebtPlanAccedeAssignSum(Map<String, Object> params);

	/**
	 * 根据相应的用户userId,加入订单对应的汇添金专属标的对应的待还金额
	 * @param params
	 * @return
	 */
		
	BigDecimal selectDebtPlanAccedeInvestSum(Map<String, Object> params);

	int updateDebtPlanAccedeLoans(DebtPlanAccede debtPlanAccede);

	/**
	 * 更新accedeBalance和repayBalance
	 * @Title updateDebtPlanAccedeBalance
	 * @param debtPlanAccede
	 * @return
	 */
	int updateDebtPlanAccedeBalance(DebtPlanAccede debtPlanAccede);
	
	/**
	 * 更新加入的RepayBalance
	 * @Title updateDebtPlanRepayBalance
	 * @param debtPlanAccede
	 * @return
	 */
	int updateDebtPlanRepayBalance(DebtPlanAccede debtPlanAccede);

	/**
	 * 清算日内还款,更新
	 * @param sellerDebtPlanAccedeNew
	 * @return
	 */
		
	int updatePlanAccedeLiquidatesFreeze(DebtPlanAccede sellerDebtPlanAccedeNew);

	/**
	 * 更新还款加入的accedeBalance,status,reinvest_status
	 * @param sellerDebtPlanAccedeNew
	 * @return
	 */
		
	int updateDebtPlanAccedeRepayFreeze(DebtPlanAccede sellerDebtPlanAccedeNew);

	/**
	 * 清算时,更新RepayMoney
	 * @Title updateDebtPlanLiquidatesBalance
	 * @param upateDebtPlanAccede
	 * @return
	 */
	int updateDebtPlanLiquidatesBalance(DebtPlanAccede upateDebtPlanAccede);

	/**
	 * 清算承接，解冻出让人账户
	 * @param sellerDebtPlanAccede
	 * @return
	 */
	int updateDebtPlanAccedeSellerUnFreeze(DebtPlanAccede sellerDebtPlanAccede);



}