package com.hyjf.admin.manager.borrow.borrowapply;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.Loan;
import com.hyjf.mybatis.model.customize.BorrowApplyCustomize;

public interface BorrowApplyService extends BaseService {

	/**
	 * 借款申请列表
	 * 
	 * @param borrowApplyCustomize
	 * @return
	 */
	public List<BorrowApplyCustomize> selectBorrowApplyList(BorrowApplyCustomize borrowApplyCustomize);

	/**
	 * 借款申请记录 总数COUNT
	 * 
	 * @param borrowApplyCustomize
	 * @return
	 */
	public Long countBorrowApply(BorrowApplyCustomize borrowApplyCustomize);

	/**
	 * 借款申请详细
	 * 
	 * @param id
	 * @return
	 */
	public Loan getLoan(String id);

	/**
	 * 借款申请更新
	 * 
	 * @param loan
	 * @return
	 */
	public int updateRecord(Loan loan);

}
