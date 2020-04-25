package com.hyjf.admin.manager.plan.statisdetail;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.PlanStatisCustomize;

@Service
public class StatisDetailServiceImpl extends BaseServiceImpl implements StatisDetailService {

	@Override
	public Long countPlanStatic(PlanStatisCustomize planStatisCustomize) {
		return this.planStatisCustomizeMapper.countPlanStatic(planStatisCustomize);
		}

	@Override
	public List<PlanStatisCustomize> selectPlanStaticList(
			PlanStatisCustomize planStatisCustomize) {
		return this.planStatisCustomizeMapper.selectProductStatisRecord(planStatisCustomize);
		}


}
