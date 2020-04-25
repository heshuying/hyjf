package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.AutoReqRepayBorrowCustomize;
import com.hyjf.mybatis.model.customize.BorrowInvestCustomize;

public interface AutoReqRepayBorrowCustomizeMapper {

	/**
	 * 当然应还款列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	List<BorrowInvestCustomize> selectBorrowInvestList(BorrowInvestCustomize borrowInvestCustomize);

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	Long countBorrowInvest(BorrowInvestCustomize borrowInvestCustomize);

	/**
	 * 导出出借明细列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	List<BorrowInvestCustomize> exportBorrowInvestList(BorrowInvestCustomize borrowInvestCustomize);

	/**
	 * 出借金额合计
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	String sumBorrowInvestAccount(BorrowInvestCustomize borrowInvestCustomize);

	/**
	 * 取得本日应还款标的列表
	 * @return
	 */
	List<AutoReqRepayBorrowCustomize> getAutoReqRepayBorrow();

}