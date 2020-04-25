package com.hyjf.admin.manager.debt.debtborrowapply;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.Loan;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowApplyCustomize;

@Service
public class DebtBorrowApplyServiceImpl extends BaseServiceImpl implements DebtBorrowApplyService {

	/**
	 * 借款明细列表
	 * 
	 * @param borrowCommonCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<DebtBorrowApplyCustomize> selectBorrowApplyList(DebtBorrowApplyCustomize debtBorrowApplyCustomize) {
		return this.debtBorrowApplyCustomizeMapper.selectBorrowApplyList(debtBorrowApplyCustomize);
	}

	/**
	 * 借款明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowApply(DebtBorrowApplyCustomize debtBorrowApplyCustomize) {
		return this.debtBorrowApplyCustomizeMapper.countBorrowApply(debtBorrowApplyCustomize);
	}

	/**
	 * 借款申请详细
	 * 
	 * @param id
	 * @return
	 */
	public Loan getLoan(String id) {
		return this.loanMapper.selectByPrimaryKey(Integer.valueOf(id));
	}

	/**
	 * 借款申请更新
	 * 
	 * @param loan
	 * @return
	 */
	public int updateRecord(Loan loan) {
		return this.loanMapper.updateByPrimaryKeySelective(loan);
	}
}
