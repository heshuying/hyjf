package com.hyjf.mybatis.mapper.customize.admin.htj;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowFirstCustomize;

public interface DebtBorrowFirstCustomizeMapper {

	/**
	 * 获取借款列表
	 * 
	 * @param borrowFirstCustomize
	 * @return
	 */
	List<DebtBorrowFirstCustomize> selectBorrowFirstList(DebtBorrowCommonCustomize debtBorrowCommonCustomize);

	/**
	 * COUNT
	 * 
	 * @param debtBorrowCommonCustomize
	 * @return
	 */
	Integer countBorrowFirst(DebtBorrowCommonCustomize debtBorrowCommonCustomize);

	/**
	 * 统计页面值总和
	 * 
	 * @param debtBorrowCommonCustomize
	 * @return
	 */
	String sumBorrowFirstAccount(DebtBorrowCommonCustomize debtBorrowCommonCustomize);

}