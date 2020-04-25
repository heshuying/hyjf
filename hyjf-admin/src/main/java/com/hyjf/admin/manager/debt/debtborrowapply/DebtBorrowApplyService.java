package com.hyjf.admin.manager.debt.debtborrowapply;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.Loan;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowApplyCustomize;

/**
 * 借款申请列表Service
 * 
 * @ClassName DebtBorrowApplyService
 * @author liuyang
 * @date 2016年9月29日 下午5:54:41
 */
public interface DebtBorrowApplyService extends BaseService {

	/**
	 * 借款申请列表
	 * 
	 * @param debtBorrowApplyCustomize
	 * @return
	 */
	public List<DebtBorrowApplyCustomize> selectBorrowApplyList(DebtBorrowApplyCustomize debtBorrowApplyCustomize);

	/**
	 * 借款申请记录 总数COUNT
	 * 
	 * @param borrowApplyCustomize
	 * @return
	 */
	public Long countBorrowApply(DebtBorrowApplyCustomize debtBorrowApplyCustomize);

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
