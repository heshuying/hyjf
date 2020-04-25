package com.hyjf.admin.manager.debt.debtborrow;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;

public interface DebtBorrowService {

	/**
	 * 合计
	 * 
	 * @return
	 */
	public Long countBorrow(DebtBorrowCommonCustomize debtBorrowCommonCustomize);

	/**
	 * 总额合计
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public BigDecimal sumAccount(DebtBorrowCommonCustomize debtborrowCommonCustomize);

	/**
	 * 借款列表
	 * 
	 * @return
	 */
	public List<DebtBorrowCustomize> selectBorrowList(DebtBorrowCommonCustomize debtBorrowCommonCustomize);

	/**
	 * 列表导出
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public List<DebtBorrowCommonCustomize> exportBorrowList(DebtBorrowCommonCustomize debtBorrowCommonCustomize);
}
