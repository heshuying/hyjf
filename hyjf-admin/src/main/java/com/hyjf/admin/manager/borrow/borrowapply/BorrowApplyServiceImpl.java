package com.hyjf.admin.manager.borrow.borrowapply;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.Loan;
import com.hyjf.mybatis.model.customize.BorrowApplyCustomize;

@Service
public class BorrowApplyServiceImpl extends BaseServiceImpl implements BorrowApplyService {

	/**
	 * 借款明细列表
	 * 
	 * @param borrowCommonCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<BorrowApplyCustomize> selectBorrowApplyList(BorrowApplyCustomize borrowApplyCustomize) {
		return this.borrowApplyCustomizeMapper.selectBorrowApplyList(borrowApplyCustomize);
	}

	/**
	 * 借款明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowApply(BorrowApplyCustomize borrowApplyCustomize) {
		return this.borrowApplyCustomizeMapper.countBorrowApply(borrowApplyCustomize);
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
