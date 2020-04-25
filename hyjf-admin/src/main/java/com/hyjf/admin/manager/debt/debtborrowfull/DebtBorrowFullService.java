package com.hyjf.admin.manager.debt.debtborrowfull;

import java.util.List;

import com.hyjf.mybatis.model.auto.DebtInvest;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowFullCustomize;

public interface DebtBorrowFullService {

	/**
	 * 复审记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowFull(DebtBorrowCommonCustomize debtBorrowCommonCustomize);

	/**
	 * 复审记录
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public List<DebtBorrowFullCustomize> selectBorrowFullList(DebtBorrowCommonCustomize debtBorrowCommonCustomize);

	/**
	 * 获取复审状态
	 * 
	 * @param record
	 */
	public boolean isBorrowStatus16(DebtBorrowFullBean borrowBean);

	/**
	 * 更新复审
	 * 
	 * @param record
	 */
	public void updateReverifyRecord(DebtBorrowFullBean borrowBean);

	/**
	 * 流标
	 * 
	 * @param record
	 */
	public void updateBorrowRecordOver(DebtBorrowFullBean borrowBean);

	/**
	 * 复审中的列表
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public List<DebtBorrowFullCustomize> selectFullList(String borrowNid, int limitStart, int limitEnd);

	/**
	 * 复审详细
	 * 
	 * @param borrowFullCustomize
	 * @return
	 */
	public DebtBorrowFullCustomize selectFullInfo(String borrowNid);

	/**
	 * 合计
	 * 
	 * @param borrowFullCustomize
	 * @return
	 */
	public DebtBorrowFullCustomize sumAmount(String borrowNid);

	/**
	 * 重新放款
	 * 
	 * @param record
	 */
	public void updateBorrowApicronRecord(DebtBorrowFullBean borrowBean);

	public List<DebtInvest> selectDebtInvestListByBorrowNid(String borrowNid, int status);

	public DebtPlan selectDebtPlanByPlanNid(String planNid);

	/**
	 * 根据项目编号检索是否有清算中的计划
	 * 
	 * @param borrowNid
	 * @return
	 */
	public int countDebtInvestListByBorrowNid(String borrowNid);
}
