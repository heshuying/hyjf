package com.hyjf.admin.manager.borrow.borrowrepaymentdetails;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.BorrowRepaymentDetailsCustomize;

public interface BorrowRepaymentDetailsService extends BaseService {

	/**
	 * 出借明细列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	public List<BorrowRepaymentDetailsCustomize> selectBorrowRepaymentDetailsList(
			BorrowRepaymentDetailsCustomize borrowRepaymentDetailsCustomize);

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRepaymentDetails(BorrowRepaymentDetailsCustomize borrowRepaymentDetailsCustomize);
	/**
	 * sum出借明细
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public BorrowRepaymentDetailsCustomize sumBorrowRepaymentDetails(BorrowRepaymentDetailsCustomize borrowRepaymentDetailsCustomize);

}
