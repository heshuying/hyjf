package com.hyjf.admin.manager.plan.raise;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.DebtPlan;

/**
 * 计划募集Service
 * 
 * @ClassName PlanRaiseService
 * @author liuyang
 * @date 2016年9月26日 下午3:02:12
 */
public interface PlanRaiseService extends BaseService {

	/**
	 * 检索募集中计划的数量
	 * 
	 * @Title countPlanRaise
	 * @param form
	 * @return
	 */
	public int countPlanRaise(PlanRaiseBean form);

	/**
	 * 检索募集中计划列表
	 * 
	 * @Title selectPlanRaiseList
	 * @param form
	 * @return
	 */
	public List<DebtPlan> selectPlanRaiseList(PlanRaiseBean form);
}
