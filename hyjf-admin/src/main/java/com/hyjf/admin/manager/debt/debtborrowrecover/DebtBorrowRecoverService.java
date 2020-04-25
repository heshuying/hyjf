package com.hyjf.admin.manager.debt.debtborrowrecover;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowRecoverCustomize;

public interface DebtBorrowRecoverService extends BaseService {

	/**
	 * 出借明细列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	public List<DebtBorrowRecoverCustomize> selectBorrowRecoverList(DebtBorrowRecoverCustomize borrowRecoverCustomize);

	/**
	 * 放款明细记录 合计
	 * 
	 * @param borrowRecoverCustomize
	 * @return
	 */
	public DebtBorrowRecoverCustomize sumBorrowRecoverList(DebtBorrowRecoverCustomize borrowRecoverCustomize);

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRecover(DebtBorrowRecoverCustomize borrowRecoverCustomize);

	/**
	 * 导出明细列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	public List<DebtBorrowRecoverCustomize> exportBorrowRecoverList(DebtBorrowRecoverCustomize borrowRecoverCustomize);

}
