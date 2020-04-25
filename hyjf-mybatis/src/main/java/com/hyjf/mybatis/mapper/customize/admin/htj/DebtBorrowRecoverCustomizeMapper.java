package com.hyjf.mybatis.mapper.customize.admin.htj;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowRecoverCustomize;


public interface DebtBorrowRecoverCustomizeMapper {

	/**
	 * 放款明细列表
	 * 
	 * @param borrowRecoverCustomize
	 * @return
	 */
	List<DebtBorrowRecoverCustomize> selectBorrowRecoverList(DebtBorrowRecoverCustomize borrowRecoverCustomize);

	/**
	 * 放款明细记录 总数COUNT
	 * 
	 * @param borrowRecoverCustomize
	 * @return
	 */
	Long countBorrowRecover(DebtBorrowRecoverCustomize borrowRecoverCustomize);

	/**
	 * 放款明细记录 合计
	 * 
	 * @param borrowRecoverCustomize
	 * @return
	 */
	DebtBorrowRecoverCustomize sumBorrowRecoverList(DebtBorrowRecoverCustomize borrowRecoverCustomize);

	/**
	 * 放款明细列表
	 * 
	 * @param borrowRecoverCustomize
	 * @return
	 */
	List<DebtBorrowRecoverCustomize> exportBorrowRecoverList(DebtBorrowRecoverCustomize borrowRecoverCustomize);

}