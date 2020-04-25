package com.hyjf.mybatis.mapper.customize;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.BorrowCreditCustomize;

public interface BorrowCreditCustomizeMapper {

	/**
	 * 获取列表
	 * 
	 * @param borrowCreditCustomize
	 * @return
	 */
	List<BorrowCreditCustomize> selectBorrowCreditList(BorrowCreditCustomize borrowCreditCustomize);

	/**
	 * COUNT
	 * 
	 * @param borrowCreditCustomize
	 * @return
	 */
	Integer countBorrowCredit(BorrowCreditCustomize borrowCreditCustomize);

	/**
	 * 导出列表
	 * 
	 * @param borrowCreditCustomize
	 * @return
	 */
	List<BorrowCreditCustomize> exportBorrowCreditList(BorrowCreditCustomize borrowCreditCustomize);

	/**
	 * 获取详细列表COUNT
	 * 
	 * @param borrowCreditCustomize
	 * @return
	 */
	Integer countBorrowCreditInfoList(BorrowCreditCustomize borrowCreditCustomize);

	/**
	 * 获取详细列表
	 * 
	 * @param borrowCreditCustomize
	 * @return
	 */
	List<BorrowCreditCustomize> selectBorrowCreditInfoList(BorrowCreditCustomize borrowCreditCustomize);

	/**
	 * 根据ID更新数据
	 * @param paramMap
	 */
	void updateParam(Map<String, Object> paramMap);

	/**
	 * 判断是否是债转的标
	 * @param borrowId
	 * @return
	 */
	int getCountByBorrowId(String borrowId);
	
	/**
	 * 获取金额合计
	 * 
	 * @param borrowCreditCustomize
	 * @return
	 */
	BorrowCreditCustomize sumBorrowCredit(BorrowCreditCustomize borrowCreditCustomize);	
	
	/**
	 * dialog获取金额合计
	 * 
	 * @param borrowCreditCustomize
	 * @return
	 */
	BorrowCreditCustomize sumBorrowCreditInfo(BorrowCreditCustomize borrowCreditCustomize);
}