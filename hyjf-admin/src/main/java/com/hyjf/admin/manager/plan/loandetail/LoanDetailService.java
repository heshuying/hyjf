package com.hyjf.admin.manager.plan.loandetail;

import java.util.HashMap;
import java.util.List;

import com.hyjf.mybatis.model.customize.CreditDetailCustomize;
import com.hyjf.mybatis.model.customize.PlanInvestCustomize;

public interface LoanDetailService {

	public List<PlanInvestCustomize> selectPlanInvestList(CreditDetailCustomize creditDetailCustomize);

	public Long countPlanInvest(CreditDetailCustomize creditDetailCustomize);

	public HashMap<String, Object> planInvestSumMap(
			CreditDetailCustomize creditDetailCustomize);
}
