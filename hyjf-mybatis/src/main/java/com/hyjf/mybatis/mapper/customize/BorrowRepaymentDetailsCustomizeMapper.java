package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.BorrowRepaymentDetailsCustomize;

public interface BorrowRepaymentDetailsCustomizeMapper {

	/**
	 * 出借明细列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	List<BorrowRepaymentDetailsCustomize> selectBorrowRepaymentDetailsList(
	        BorrowRepaymentDetailsCustomize borrowRepaymentDetailsCustomize);

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	Long countBorrowRepaymentDetails(BorrowRepaymentDetailsCustomize borrowRepaymentDetailsCustomize);

	/**
	 * sum出借明细记录 总数
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	BorrowRepaymentDetailsCustomize sumBorrowRepaymentDetails(BorrowRepaymentDetailsCustomize borrowRepaymentDetailsCustomize);

}