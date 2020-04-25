package com.hyjf.admin.manager.plan.creditdetail;

import java.util.HashMap;
import java.util.List;

import com.hyjf.mybatis.model.customize.CreditDetailCustomize;
import com.hyjf.mybatis.model.customize.PlanInvestCustomize;

public interface CreditDetailService {

	public List<PlanInvestCustomize> selectPlanInvestList(CreditDetailCustomize creditDetailCustomize);

	public Long countPlanInvest(CreditDetailCustomize creditDetailCustomize);

	public HashMap<String, Object> planInvestSumMap(
			CreditDetailCustomize creditDetailCustomize);

	public Long countPlanInvestNew(CreditDetailCustomize creditDetailCustomize);

	public HashMap<String, Object> creditInvestSumMapNew(
			CreditDetailCustomize creditDetailCustomize);

	public List<PlanInvestCustomize> selectPlanInvestListNew(
			CreditDetailCustomize creditDetailCustomize);
}
