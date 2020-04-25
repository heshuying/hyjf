package com.hyjf.mybatis.mapper.customize.admin.htj;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowRepaymentInfoCustomize;

public interface DebtBorrowRepaymentInfoCustomizeMapper {

	/**
	 * 出借明细列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	List<DebtBorrowRepaymentInfoCustomize> selectBorrowRepaymentInfoList(DebtBorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize);

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	Long countBorrowRepaymentInfo(DebtBorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize);

	/**
	 * sum出借明细记录 总数
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	DebtBorrowRepaymentInfoCustomize sumBorrowRepaymentInfo(DebtBorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize);

}