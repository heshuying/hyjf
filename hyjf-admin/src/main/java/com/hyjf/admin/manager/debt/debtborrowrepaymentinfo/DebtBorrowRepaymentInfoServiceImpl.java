package com.hyjf.admin.manager.debt.debtborrowrepaymentinfo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowRepaymentInfoCustomize;

@Service
public class DebtBorrowRepaymentInfoServiceImpl extends BaseServiceImpl implements DebtBorrowRepaymentInfoService {

	/**
	 * 出借明细列表
	 * 
	 * @param borrowCommonCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<DebtBorrowRepaymentInfoCustomize> selectBorrowRepaymentInfoList(
			DebtBorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize) {
		return this.debtBorrowRepaymentInfoCustomizeMapper.selectBorrowRepaymentInfoList(borrowRepaymentInfoCustomize);
	}

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRepaymentInfo(DebtBorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize) {
		return this.debtBorrowRepaymentInfoCustomizeMapper.countBorrowRepaymentInfo(borrowRepaymentInfoCustomize);
	}

	@Override
	public DebtBorrowRepaymentInfoCustomize sumBorrowRepaymentInfo(
			DebtBorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize) {
		return this.debtBorrowRepaymentInfoCustomizeMapper.sumBorrowRepaymentInfo(borrowRepaymentInfoCustomize);
	}

}
