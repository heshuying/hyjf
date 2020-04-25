package com.hyjf.bank.service.borrow.issue;

import com.hyjf.bank.service.borrow.AssetService;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;

public interface AutoIssueService extends AssetService {

	/**
	 * 更新标的计划编号，redis计划
	 * @param borrow
	 * @return
	 */
	boolean updateIssueBorrow(BorrowWithBLOBs borrow, String planNid,HjhPlanAsset hjhPlanAsset);

	/**
	 * 更新债转标的计划编号，redis计划
	 * @param planNid
	 * @return
	 */
	boolean updateIssueCredit(HjhDebtCredit credit, String planNid);

	/**
	 * 获取一个债转标的
	 * @param creditNid
	 * @return
	 */
	HjhDebtCredit mainGetCreditByNid(String creditNid);

}
