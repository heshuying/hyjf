package com.hyjf.admin.manager.debt.debtborrowrepaymentinfo.debtinfolist;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowRepaymentInfoListCustomize;

@Service
public class DebtBorrowRepaymentInfoListServiceImpl extends BaseServiceImpl implements DebtBorrowRepaymentInfoListService {

	/**
	 * 出借明细列表
	 * 
	 * @param borrowCommonCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<DebtBorrowRepaymentInfoListCustomize> selectBorrowRepaymentInfoListList(
			DebtBorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize) {
		return this.debtBorrowRepaymentInfoListCustomizeMapper
				.selectBorrowRepaymentInfoListList(borrowRepaymentInfoListCustomize);
	}

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRepaymentInfoList(DebtBorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize) {
		return this.debtBorrowRepaymentInfoListCustomizeMapper
				.countBorrowRepaymentInfoList(borrowRepaymentInfoListCustomize);
	}

	@Override
	public DebtBorrowRepaymentInfoListCustomize sumBorrowRepaymentInfoList(
			DebtBorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize) {
		return this.debtBorrowRepaymentInfoListCustomizeMapper.sumBorrowRepaymentInfoList(borrowRepaymentInfoListCustomize);
	}

}