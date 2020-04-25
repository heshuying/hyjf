package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowFirstCustomize;

public interface BorrowFirstCustomizeMapper {

	/**
	 * 获取借款列表
	 * 
	 * @param borrowFirstCustomize
	 * @return
	 */
	List<BorrowFirstCustomize> selectBorrowFirstList(BorrowCommonCustomize corrowCommonCustomize);

	/**
	 * COUNT
	 * 
	 * @param borrowFirstCustomize
	 * @return
	 */
	Integer countBorrowFirst(BorrowCommonCustomize corrowCommonCustomize);

	/**
	 * 统计页面值总和
	 * @param corrowCommonCustomize
	 * @return
	 */
	String sumBorrowFirstAccount(BorrowCommonCustomize corrowCommonCustomize);

}