package com.hyjf.admin.manager.borrow.borrowrepayment.plan;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.BorrowRepaymentPlanCustomize;

public interface BorrowRepaymentPlanService extends BaseService {

	/**
	 * 出借计划列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	public List<BorrowRepaymentPlanCustomize> selectBorrowRepaymentPlanList(
			BorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize);
	/**
     * 导出还款计划列表
     * zdj
     * @param alllBorrowCustomize
     * @return
     */
    public List<BorrowRepaymentPlanCustomize> exportRepayClkActBorrowRepaymentInfoList(
            BorrowRepaymentPlanCustomize borrowRepaymentCustomize);

	/**
	 * 出借计划记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRepaymentPlan(BorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize);

	/**
	 * sum出借计划
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public BorrowRepaymentPlanCustomize sumBorrowRepaymentPlan(
			BorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize);
}
