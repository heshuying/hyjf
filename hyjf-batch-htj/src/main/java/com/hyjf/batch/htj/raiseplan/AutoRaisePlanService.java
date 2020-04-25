package com.hyjf.batch.htj.raiseplan;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.DebtPlanWithBLOBs;

/**
 * 募集中的计划状态变更定时Service
 * 
 * @ClassName AutoRaisePlanService
 * @author liuyang
 * @date 2016年10月10日 上午10:06:33
 */
public interface AutoRaisePlanService extends BaseService {

	/**
	 * 检索满标,募集中的计划
	 * 
	 * @Title selectFullPlanList
	 * @return
	 */
	public List<DebtPlanWithBLOBs> selectFullPlanList();

	/**
	 * 检索募集未满,购买结束时间到期的计划
	 * 
	 * @Title selectNotFullPlanList
	 * @return
	 */
	public List<DebtPlanWithBLOBs> selectNotFullPlanList();

	/**
	 * 检索没有加入金额,购买结束时间到期的计划
	 * 
	 * @Title selectEmptyPlanList
	 * @return
	 */
	public List<DebtPlanWithBLOBs> selectEmptyPlanList();

	/**
	 * 更新计划状态:募集中到锁定中
	 * 
	 * @Title updatePlanStatus
	 * @param plan
	 * @return
	 */
	public int updatePlanStatus(DebtPlanWithBLOBs plan);

	/**
	 * 更新没有募集到资金的计划状态:募集中到流标
	 * 
	 * @Title updateEmptyPlanStatus
	 * @param plan
	 * @return
	 */
	public int updateEmptyPlanStatus(DebtPlanWithBLOBs plan);

	/**
	 * 更新没有募集满的计划状态:募集中到锁定中,应清算时间
	 * 
	 * @Title updatePlanStatusAndLiquidateTime
	 * @param plan
	 * @return
	 */
	public int updatePlanStatusAndLiquidateTime(DebtPlanWithBLOBs plan);

	/**
	 * 进入锁定期发送短信
	 * 
	 * @Title sendMsg
	 * @param plan
	 * @param isFull
	 */
	public void sendMsg(DebtPlanWithBLOBs plan, boolean isFull);

}
