package com.hyjf.admin.manager.debt.debtborrowrepaymentinfo.debtinfolist;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowRepaymentInfoListCustomize;

public interface DebtBorrowRepaymentInfoListService extends BaseService {

	/**
	 * 出借明细详情列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	public List<DebtBorrowRepaymentInfoListCustomize> selectBorrowRepaymentInfoListList(
			DebtBorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize);

	/**
	 * 出借明细详情记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRepaymentInfoList(DebtBorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize);

	/**
	 * sum出借明细详情
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public DebtBorrowRepaymentInfoListCustomize sumBorrowRepaymentInfoList(
			DebtBorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize);
}
