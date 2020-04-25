package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.BorrowRegistCustomize;

public interface BorrowRegistCustomizeMapper {

	/**
	 * 获取借款列表
	 * 
	 * @param borrowFirstCustomize
	 * @return
	 */
	List<BorrowRegistCustomize> selectBorrowRegistList(BorrowCommonCustomize corrowCommonCustomize);

	/**
	 * COUNT
	 * 
	 * @param borrowFirstCustomize
	 * @return
	 */
	Integer countBorrowRegist(BorrowCommonCustomize corrowCommonCustomize);

	/**
	 * 统计页面值总和
	 * @param corrowCommonCustomize
	 * @return
	 */
	String sumBorrowRegistAccount(BorrowCommonCustomize corrowCommonCustomize);

}