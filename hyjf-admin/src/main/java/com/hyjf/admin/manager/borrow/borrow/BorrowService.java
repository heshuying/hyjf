package com.hyjf.admin.manager.borrow.borrow;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;

public interface BorrowService {

	/**
	 * 合计
	 * 
	 * @return
	 */
	public Long countBorrow(BorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 总额合计
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public BigDecimal sumAccount(BorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 借款列表
	 * 
	 * @return
	 */
	public List<BorrowCustomize> selectBorrowList(BorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 列表导出
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public List<BorrowCommonCustomize> exportBorrowList(BorrowCommonCustomize borrowCommonCustomize);
}
