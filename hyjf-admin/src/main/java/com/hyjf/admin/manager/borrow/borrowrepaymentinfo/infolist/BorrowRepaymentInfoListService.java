package com.hyjf.admin.manager.borrow.borrowrepaymentinfo.infolist;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.BorrowRepaymentInfoListCustomize;

public interface BorrowRepaymentInfoListService extends BaseService {

	/**
	 * 出借明细详情列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	public List<BorrowRepaymentInfoListCustomize> selectBorrowRepaymentInfoListList(
			BorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize);

	/**
	 * 出借明细详情记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRepaymentInfoList(BorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize);

	/**
	 * sum出借明细详情
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public BorrowRepaymentInfoListCustomize sumBorrowRepaymentInfoList(
			BorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize);
}
