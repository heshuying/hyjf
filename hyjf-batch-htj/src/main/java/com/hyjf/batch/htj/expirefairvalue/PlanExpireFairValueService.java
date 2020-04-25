package com.hyjf.batch.htj.expirefairvalue;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.DebtPlan;

/**
 * 到期公允价值的计算Service
 * 
 * @ClassName PlanExpireFairValueService
 * @author liuyang
 * @date 2016年11月22日 下午1:52:27
 */
public interface PlanExpireFairValueService extends BaseService {

	/**
	 * 检索锁定中的计划列表
	 * 
	 * @Title selectLockPlanList
	 * @return
	 */
	public List<DebtPlan> selectLockPlanList();

	/**
	 * 计算到期公允价值
	 * 
	 * @Title calculation
	 * @param plan
	 */
	public void calculation(DebtPlan plan) throws Exception;

}
