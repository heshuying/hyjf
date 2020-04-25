package com.hyjf.admin.manager.debt.debtborrowrepayment.plan;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowRepaymentPlanCustomize;

@Service
public class DebtBorrowRepaymentPlanServiceImpl extends BaseServiceImpl implements DebtBorrowRepaymentPlanService {

	@Override
	public List<DebtBorrowRepaymentPlanCustomize> selectBorrowRepaymentPlanList(DebtBorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize) {
		return this.debtBorrowRepaymentPlanCustomizeMapper.selectBorrowRepaymentPlanList(borrowRepaymentPlanCustomize);
	}

	@Override
	public Long countBorrowRepaymentPlan(DebtBorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize) {
		return this.debtBorrowRepaymentPlanCustomizeMapper.countBorrowRepaymentPlan(borrowRepaymentPlanCustomize);
	}

	@Override
	public DebtBorrowRepaymentPlanCustomize sumBorrowRepaymentPlan(DebtBorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize) {
		return this.debtBorrowRepaymentPlanCustomizeMapper.sumBorrowRepaymentPlan(borrowRepaymentPlanCustomize);
	}

}
