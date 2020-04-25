package com.hyjf.mybatis.mapper.customize.batch;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.batch.BatchDebtBorrowCommonCustomize;

public interface BatchDebtBorrowCustomizeMapper {

	/**
	 * 查询出到期但是未满标的标的,给其发短信
	 * 
	 * @return
	 */
	List<BatchDebtBorrowCommonCustomize> searchNotFullBorrowMsg();

	/**
	 * @param updateOfLoansBorrow
	 * @return
	 */
	int updateOfBorrow(Map<String, Object> borrow);

	/**
	 * @param updateOfRepayTender
	 * @return
	 */
	int updateOfFullBorrow(Map<String, Object> borrow);

}