package com.hyjf.batch.htj.unableliquidates;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.DebtPlan;

public interface UnableLiquidatesService extends BaseService {
	
	/**
	 * 查询相应的募集中的计划（按buy_begin_time申购开始时间升序排列）
	 * 
	 * @param planStatus
	 * 
	 * @return
	 */

	public List<DebtPlan> selectDebtPlanLiquidates();

	/**
	 * 根据计划nid查询相应的可出借金额
	 * @param planNid
	 * @return
	 */
	public BigDecimal countDebtCreditSum(String planNid);

	/**
	 * 
	 * @param planNid
	 * @param total
	 */
	public void sendSms(String planNid, BigDecimal total);

	/**
	 * 
	 * @param planNid
	 * @param total
	 */
	public void sendEmail(String planNid, BigDecimal total);
	
	
}
