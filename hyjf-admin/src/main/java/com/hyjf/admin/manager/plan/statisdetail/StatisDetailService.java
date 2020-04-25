package com.hyjf.admin.manager.plan.statisdetail;

import java.util.List;

import com.hyjf.mybatis.model.customize.PlanStatisCustomize;

public interface StatisDetailService {

	public Long countPlanStatic(PlanStatisCustomize planStatisCustomize);

	public List<PlanStatisCustomize> selectPlanStaticList(
			PlanStatisCustomize planStatisCustomize);
}
