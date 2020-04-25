package com.hyjf.mybatis.mapper.customize.admin.htj;

import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowInvestCustomize;

import java.util.List;


public interface DebtBorrowInvestCustomizeMapper {

	/**
	 * 出借明细列表
	 * 
	 * @param debtBorrowInvestCustomize
	 * @return
	 */
	List<DebtBorrowInvestCustomize> selectBorrowInvestList(DebtBorrowInvestCustomize debtBorrowInvestCustomize);

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param debtBorrowInvestCustomize
	 * @return
	 */
	Long countBorrowInvest(DebtBorrowInvestCustomize debtBorrowInvestCustomize);

	/**
	 * 导出出借明细列表
	 * 
	 * @param debtBorrowInvestCustomize
	 * @return
	 */
	List<DebtBorrowInvestCustomize> exportBorrowInvestList(DebtBorrowInvestCustomize debtBorrowInvestCustomize);

	/**
	 * 出借金额合计
	 * 
	 * @param debtBorrowInvestCustomize
	 * @return
	 */
	String selectBorrowInvestAccount(DebtBorrowInvestCustomize debtBorrowInvestCustomize);

}