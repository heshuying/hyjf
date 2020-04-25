package com.hyjf.batch.htj.planActualApr;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.DebtPlan;

public interface PlanActualAprService extends BaseService {

	List<DebtPlan> selectPlanAllInLock();

	Long updatePlanActualApr(String planNid);
	
}
