package com.hyjf.batch.htj.releaseplan;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.DebtPlan;

/**
 * 自动发布计划Service
 * 
 * @ClassName AutoReleasePlanService
 * @author liuyang
 * @date 2016年10月9日 下午5:43:56
 */
public interface AutoReleasePlanService extends BaseService {
	/**
	 * 检索待开放计划列表
	 * 
	 * @Title selectNeedReleasePlanList
	 * @return
	 */
	public List<DebtPlan> selectNeedReleasePlanList();

	/**
	 * 更新计划的计划状态
	 * 
	 * @Title updateReleasePlan
	 * @param plan
	 */
	public int updateReleasePlan(DebtPlan plan);
}
