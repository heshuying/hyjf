package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.AdminRecoverExceptionCustomize;

public interface AdminRecoverExceptionCustomizeMapper {

	/**
	 * 总数COUNT
	 * 
	 * @param record
	 * @return
	 */
	Long queryCount(AdminRecoverExceptionCustomize record);

	/**
	 * 记录
	 * 
	 * @param record
	 * @return
	 */
	List<AdminRecoverExceptionCustomize> queryRecordList(AdminRecoverExceptionCustomize record);

	/**
	 * 放款明细列表
	 * 
	 * @param record
	 * @return
	 */
	List<AdminRecoverExceptionCustomize> queryBorrowRecoverList(AdminRecoverExceptionCustomize record);

	/**
	 * 放款明细记录 总数COUNT
	 * 
	 * @param record
	 * @return
	 */
	Long queryCountBorrowRecover(AdminRecoverExceptionCustomize record);

	/**
	 * 放款明细记录 合计
	 * 
	 * @param record
	 * @return
	 */
	AdminRecoverExceptionCustomize querySumBorrowRecoverList(AdminRecoverExceptionCustomize record);
}