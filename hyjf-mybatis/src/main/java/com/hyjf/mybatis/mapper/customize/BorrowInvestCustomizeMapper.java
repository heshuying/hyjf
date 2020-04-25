package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.customize.BorrowInvestCustomize;

import java.util.List;

public interface BorrowInvestCustomizeMapper {

	/**
	 * 出借明细列表
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 */
	List<BorrowInvestCustomize> selectBorrowInvestList(BorrowInvestCustomize borrowInvestCustomize);

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 */
	Long countBorrowInvest(BorrowInvestCustomize borrowInvestCustomize);

	/**
	 * 导出出借明细列表
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 */
	List<BorrowInvestCustomize> exportBorrowInvestList(BorrowInvestCustomize borrowInvestCustomize);

	/**
	 * 出借金额合计
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 */
	String selectBorrowInvestAccount(BorrowInvestCustomize borrowInvestCustomize);
	
	/**
	 * 出借金额合计值取得
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 */
	String sumBorrowInvest(BorrowInvestCustomize borrowInvestCustomize);

}