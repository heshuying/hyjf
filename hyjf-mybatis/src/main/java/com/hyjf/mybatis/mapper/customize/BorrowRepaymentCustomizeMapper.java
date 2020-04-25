package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.BorrowRepaymentCustomize;
import com.hyjf.mybatis.model.customize.HjhCreditTenderCustomize;

public interface BorrowRepaymentCustomizeMapper {

	/**
	 * 出借明细列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	List<BorrowRepaymentCustomize> selectBorrowRepaymentList(BorrowRepaymentCustomize borrowRepaymentCustomize);
	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	Long countBorrowRepayment(BorrowRepaymentCustomize borrowRepaymentCustomize);
	
	/**
	 * sum出借明细记录 总数
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	BorrowRepaymentCustomize sumBorrowRepayment(BorrowRepaymentCustomize borrowRepaymentCustomize);
	
	/**
	 * 汇计划还款信息记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	Long countHjhBorrowRepayment(BorrowRepaymentCustomize borrowRepaymentCustomize);
	
	/**
	 * 汇计划还款信息列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	List<BorrowRepaymentCustomize> selectHjhBorrowRepaymentList(BorrowRepaymentCustomize borrowRepaymentCustomize);

	/**
	 * 汇计划还款信息列表
	 * 
	 * @param borrowRepaymentCustomize
	 * @return
	 */
	BorrowRepaymentCustomize sumHjhBorrowRepayment(BorrowRepaymentCustomize borrowRepaymentCustomize);
	
	/**
	 * (债转)汇计划还款信息记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return 
	 */
	Long countHjhCreditBorrowRepayment(BorrowRepaymentCustomize borrowRepaymentCustomize);
	
	Long countHjhCreditBorrowTender(HjhCreditTenderCustomize borrowRepaymentCustomize);
	
	/**
	 * (债转)汇计划还款信息列表
	 * 
	 * @param alllBorrowCustomize
	 * @return 
	 */ 
	List<BorrowRepaymentCustomize> selectHjhCreditBorrowRepaymentList(BorrowRepaymentCustomize borrowRepaymentCustomize);
	List<HjhCreditTenderCustomize> selectHjhCreditBorrowTendertList(HjhCreditTenderCustomize borrowRepaymentCustomize);
	
	
	
}