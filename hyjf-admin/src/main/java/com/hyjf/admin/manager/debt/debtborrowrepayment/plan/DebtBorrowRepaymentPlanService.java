package com.hyjf.admin.manager.debt.debtborrowrepayment.plan;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowRepaymentPlanCustomize;

public interface DebtBorrowRepaymentPlanService extends BaseService {

	/**
	 * 出借计划列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	public List<DebtBorrowRepaymentPlanCustomize> selectBorrowRepaymentPlanList(
			DebtBorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize);

	/**
	 * 出借计划记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRepaymentPlan(DebtBorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize);

	/**
	 * sum出借计划
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public DebtBorrowRepaymentPlanCustomize sumBorrowRepaymentPlan(
			DebtBorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize);
}
