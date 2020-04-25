package com.hyjf.admin.manager.borrow.borrowrepaymentinfo.infolist;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.BorrowRepaymentInfoListCustomize;

@Service
public class BorrowRepaymentInfoListServiceImpl extends BaseServiceImpl implements BorrowRepaymentInfoListService {

	/**
	 * 出借明细列表
	 * 
	 * @param borrowCommonCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<BorrowRepaymentInfoListCustomize> selectBorrowRepaymentInfoListList(BorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize) {
		return this.borrowRepaymentInfoListCustomizeMapper.selectBorrowRepaymentInfoListList(borrowRepaymentInfoListCustomize);
	}

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRepaymentInfoList(BorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize) {
		return this.borrowRepaymentInfoListCustomizeMapper.countBorrowRepaymentInfoList(borrowRepaymentInfoListCustomize);
	}

	@Override
	public BorrowRepaymentInfoListCustomize sumBorrowRepaymentInfoList(BorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize) {
		return this.borrowRepaymentInfoListCustomizeMapper.sumBorrowRepaymentInfoList(borrowRepaymentInfoListCustomize);
	}

}