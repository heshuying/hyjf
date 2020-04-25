package com.hyjf.mybatis.mapper.customize.admin.htj;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowRepaymentPlanCustomize;


public interface DebtBorrowRepaymentPlanCustomizeMapper {

	/**
	 * 出借计划列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	List<DebtBorrowRepaymentPlanCustomize> selectBorrowRepaymentPlanList(
			DebtBorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize);

	/**
	 * 出借计划记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	Long countBorrowRepaymentPlan(DebtBorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize);

	/**
	 * sum出借计划
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	DebtBorrowRepaymentPlanCustomize sumBorrowRepaymentPlan(DebtBorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize);

}