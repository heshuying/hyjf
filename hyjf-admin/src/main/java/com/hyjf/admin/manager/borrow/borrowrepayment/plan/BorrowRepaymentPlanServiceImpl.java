package com.hyjf.admin.manager.borrow.borrowrepayment.plan;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.BorrowRepaymentPlanCustomize;

@Service
public class BorrowRepaymentPlanServiceImpl extends BaseServiceImpl implements BorrowRepaymentPlanService {

	@Override
	public List<BorrowRepaymentPlanCustomize> selectBorrowRepaymentPlanList(
			BorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize) {
		return this.borrowRepaymentPlanCustomizeMapper.selectBorrowRepaymentPlanList(borrowRepaymentPlanCustomize);
	}
	/**
     * 导出还款计划列表
     * zdj
     * @param alllBorrowCustomize
     * @return
     */
    @Override
    public List<BorrowRepaymentPlanCustomize> exportRepayClkActBorrowRepaymentInfoList(
            BorrowRepaymentPlanCustomize borrowRepaymentCustomize) {
        return this.borrowRepaymentPlanCustomizeMapper.exportRepayClkActBorrowRepaymentInfoList(borrowRepaymentCustomize);
    }
	@Override
	public Long countBorrowRepaymentPlan(BorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize) {
		return this.borrowRepaymentPlanCustomizeMapper.countBorrowRepaymentPlan(borrowRepaymentPlanCustomize);
	}

	@Override
	public BorrowRepaymentPlanCustomize sumBorrowRepaymentPlan(
			BorrowRepaymentPlanCustomize borrowRepaymentPlanCustomize) {
		return this.borrowRepaymentPlanCustomizeMapper.sumBorrowRepaymentPlan(borrowRepaymentPlanCustomize);
	}

}
