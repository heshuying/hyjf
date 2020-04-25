package com.hyjf.admin.manager.debt.debtborrowrepaymentinfo;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowRepaymentInfoCustomize;

public interface DebtBorrowRepaymentInfoService extends BaseService {

	/**
	 * 出借明细列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	public List<DebtBorrowRepaymentInfoCustomize> selectBorrowRepaymentInfoList(DebtBorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize);

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRepaymentInfo(DebtBorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize);

	/**
	 * sum出借明细
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public DebtBorrowRepaymentInfoCustomize sumBorrowRepaymentInfo(DebtBorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize);

}
