package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.BorrowRepaymentPlanCustomize;

public interface BorrowRepaymentPlanCustomizeMapper {

	/**
	 * 出借计划列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	List<BorrowRepaymentPlanCustomize> selectBorrowRepaymentPlanList(
			BorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize);

	/**
     * 导出还款计划列表
     * zdj
     * @param alllBorrowCustomize
     * @return
     */
    List<BorrowRepaymentPlanCustomize> exportRepayClkActBorrowRepaymentInfoList(
            BorrowRepaymentPlanCustomize borrowRepaymentCustomize);
	/**
	 * 出借计划记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	Long countBorrowRepaymentPlan(BorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize);

	/**
	 * sum出借计划
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	BorrowRepaymentPlanCustomize sumBorrowRepaymentPlan(BorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize);

}