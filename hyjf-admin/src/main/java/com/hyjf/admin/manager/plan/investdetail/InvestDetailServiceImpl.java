package com.hyjf.admin.manager.plan.investdetail;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.CreditDetailCustomize;
import com.hyjf.mybatis.model.customize.PlanInvestCustomize;

@Service
public class InvestDetailServiceImpl extends BaseServiceImpl implements InvestDetailService {

	@Override
	public List<PlanInvestCustomize> selectPlanInvestList(CreditDetailCustomize creditDetailCustomize) {
		return planLockCustomizeMapper.selectCreditInvestList(creditDetailCustomize);
	}

	@Override
	public Long countPlanInvest(CreditDetailCustomize creditDetailCustomize) {
		
		return planLockCustomizeMapper.countCreditInvest(creditDetailCustomize);
		
	}

	@Override
	public HashMap<String, Object> planInvestSumMap(
			CreditDetailCustomize creditDetailCustomize) {
		return planLockCustomizeMapper.creditInvestSumMap(creditDetailCustomize);
	}

}
