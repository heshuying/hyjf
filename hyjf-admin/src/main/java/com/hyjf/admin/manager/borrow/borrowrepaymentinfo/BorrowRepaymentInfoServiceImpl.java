package com.hyjf.admin.manager.borrow.borrowrepaymentinfo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.BorrowRepaymentInfoCustomize;

@Service
public class BorrowRepaymentInfoServiceImpl extends BaseServiceImpl implements BorrowRepaymentInfoService {

	/**
	 * 出借明细列表
	 * 
	 * @param borrowCommonCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<BorrowRepaymentInfoCustomize> selectBorrowRepaymentInfoList(
			BorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize) {
		return this.borrowRepaymentInfoCustomizeMapper.selectBorrowRepaymentInfoList(borrowRepaymentInfoCustomize);
	}

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRepaymentInfo(BorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize) {
		return this.borrowRepaymentInfoCustomizeMapper.countBorrowRepaymentInfo(borrowRepaymentInfoCustomize);
	}

	@Override
	public BorrowRepaymentInfoCustomize sumBorrowRepaymentInfo(
			BorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize) {
		return this.borrowRepaymentInfoCustomizeMapper.sumBorrowRepaymentInfo(borrowRepaymentInfoCustomize);
	}

	@Override
	public List<BorrowRepaymentInfoCustomize> selectBorrowRepaymentInfoListForView(BorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize) { 
		return this.borrowRepaymentInfoCustomizeMapper.selectBorrowRepaymentInfoListForView(borrowRepaymentInfoCustomize);
	}

}
