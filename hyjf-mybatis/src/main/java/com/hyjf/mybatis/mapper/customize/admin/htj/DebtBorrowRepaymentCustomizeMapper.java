package com.hyjf.mybatis.mapper.customize.admin.htj;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowRepaymentCustomize;

public interface DebtBorrowRepaymentCustomizeMapper {

	/**
	 * 出借明细列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	List<DebtBorrowRepaymentCustomize> selectBorrowRepaymentList(DebtBorrowRepaymentCustomize borrowRepaymentCustomize);

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	Long countBorrowRepayment(DebtBorrowRepaymentCustomize borrowRepaymentCustomize);

	/**
	 * sum出借明细记录 总数
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	DebtBorrowRepaymentCustomize sumBorrowRepayment(DebtBorrowRepaymentCustomize borrowRepaymentCustomize);

}