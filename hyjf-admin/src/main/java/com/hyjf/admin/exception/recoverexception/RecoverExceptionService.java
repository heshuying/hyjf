package com.hyjf.admin.exception.recoverexception;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.AdminRecoverExceptionCustomize;

public interface RecoverExceptionService {
	/**
	 * 总数COUNT
	 * 
	 * @param record
	 * @return
	 * @author Administrator
	 */
	public Long queryCount(AdminRecoverExceptionCustomize record);

	/**
	 * 记录
	 * 
	 * @param record
	 * @return
	 * @author Administrator
	 */
	public List<AdminRecoverExceptionCustomize> queryRecordList(AdminRecoverExceptionCustomize record);

	/**
	 * 重新放款
	 * 
	 * @param record
	 */
	public void updateBorrowApicronRecord(RecoverExceptionBean record);

	/**
	 * 出借明细列表
	 * 
	 * @param borrowCommonCustomize
	 * @return
	 * @author Administrator
	 */
	public List<AdminRecoverExceptionCustomize> queryBorrowRecoverList(AdminRecoverExceptionCustomize record);

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param record
	 * @return
	 */
	public Long queryCountBorrowRecover(AdminRecoverExceptionCustomize record);

	/**
	 * 放款明细记录 合计
	 * 
	 * @param record
	 * @return
	 * @author Administrator
	 */
	public AdminRecoverExceptionCustomize querySumBorrowRecoverList(AdminRecoverExceptionCustomize record);
}
