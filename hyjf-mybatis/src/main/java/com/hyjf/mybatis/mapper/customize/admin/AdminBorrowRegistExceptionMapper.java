package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.BorrowRegistCustomize;

public interface AdminBorrowRegistExceptionMapper {

	/**
	 * 获取借款列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	List<BorrowRegistCustomize> selectBorrowList(BorrowCommonCustomize borrowCommonCustomize);

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	Long countBorrow(BorrowCommonCustomize borrowCommonCustomize);

}