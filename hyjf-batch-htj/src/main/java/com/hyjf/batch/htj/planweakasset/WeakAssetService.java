package com.hyjf.batch.htj.planweakasset;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.DebtPlan;

public interface WeakAssetService extends BaseService {
	
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
	 * @param minSurplusInvestAccount
	 * @return
	 */

	public int countDebtPlanAccede(String planNid, BigDecimal minSurplusInvestAccount, int cycleTimes);

	/**
	 * 发送短信
	 * @param planNid
	 * @param count
	 * @param cycleTimes 
	 */
		
	public void sendSms(String planNid, int count, int cycleTimes);

	/**
	 * @param planNid
	 * @param count
	 * @param cycleTimes 
	 */
		
	public void sendEmail(String planNid, int count, int cycleTimes);
}
