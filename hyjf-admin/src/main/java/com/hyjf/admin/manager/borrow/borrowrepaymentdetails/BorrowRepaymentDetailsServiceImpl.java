package com.hyjf.admin.manager.borrow.borrowrepaymentdetails;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.BorrowRepaymentDetailsCustomize;

@Service
public class BorrowRepaymentDetailsServiceImpl extends BaseServiceImpl implements BorrowRepaymentDetailsService {

	/**
	 * 出借明细列表
	 * 
	 * @param borrowCommonCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<BorrowRepaymentDetailsCustomize> selectBorrowRepaymentDetailsList(
	        BorrowRepaymentDetailsCustomize borrowRepaymentDetailsCustomize) {
		return this.borrowRepaymentDetailsCustomizeMapper.selectBorrowRepaymentDetailsList(borrowRepaymentDetailsCustomize);
	}

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRepaymentDetails(BorrowRepaymentDetailsCustomize borrowRepaymentDetailsCustomize) {
		return this.borrowRepaymentDetailsCustomizeMapper.countBorrowRepaymentDetails(borrowRepaymentDetailsCustomize);
	}

	@Override
	public BorrowRepaymentDetailsCustomize sumBorrowRepaymentDetails(
	    BorrowRepaymentDetailsCustomize borrowRepaymentDetailsCustomize) {
		return this.borrowRepaymentDetailsCustomizeMapper.sumBorrowRepaymentDetails(borrowRepaymentDetailsCustomize);
	}

}
