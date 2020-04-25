package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.customize.BorrowRecoverCustomize;
import com.hyjf.mybatis.model.customize.BorrowRecoverLatestCustomize;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BorrowRecoverCustomizeMapper {

	/**
	 * 放款明细列表
	 * 
	 * @param borrowRecoverCustomize
	 * @return
	 */
	List<BorrowRecoverCustomize> selectBorrowRecoverList(BorrowRecoverCustomize borrowRecoverCustomize);

	/**
	 * 放款明细记录 总数COUNT
	 * 
	 * @param borrowRecoverCustomize
	 * @return
	 */
	Long countBorrowRecover(BorrowRecoverCustomize borrowRecoverCustomize);

	/**
	 * 放款明细记录 合计
	 * 
	 * @param borrowRecoverCustomize
	 * @return
	 */
	BorrowRecoverCustomize sumBorrowRecoverList(BorrowRecoverCustomize borrowRecoverCustomize);

	/**
	 * 放款明细列表
	 * 
	 * @param borrowRecoverCustomize
	 * @return
	 */
	List<BorrowRecoverCustomize> exportBorrowRecoverList(BorrowRecoverCustomize borrowRecoverCustomize);
	
	/**
	 * 
	 * 近期还款项目列表
	 * @author hsy
	 * @param paraMap
	 * @return
	 */
	List<BorrowRecoverLatestCustomize> selectLatestBorrowRecoverList(Map<String, Object> paraMap);

	/**
	 * 根据放款表查询未还本息
	 * @param borrowNid
	 * @return
	 */
	BorrowRecover selectSumRecoverWait(String borrowNid);

	/**
	 * 根据放款表查询未还本息
	 * @param borrowNid
	 * @return
	 */
	BorrowRecover selectSumRecoverPlanWait(String borrowNid);

	/**
	 * 放款服务费+还款服务费
	 * @param borrowNid
	 * @return
	 */
	BigDecimal selectServiceCostSum(String borrowNid);
}