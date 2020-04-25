package com.hyjf.admin.manager.borrow.borrowrepaymentinfo;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.BorrowRepaymentInfoCustomize;

public interface BorrowRepaymentInfoService extends BaseService {

	/**
	 * 出借明细列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	public List<BorrowRepaymentInfoCustomize> selectBorrowRepaymentInfoList(
			BorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize);

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRepaymentInfo(BorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize);
	/**
	 * sum出借明细
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public BorrowRepaymentInfoCustomize sumBorrowRepaymentInfo(BorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize);
	
	/**
	 * 还款明细列表查询(优化) BY LIBIN
	 * 
	 * @param borrowRepaymentInfoCustomize
	 * @return
	 */
	public List<BorrowRepaymentInfoCustomize> selectBorrowRepaymentInfoListForView(BorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize);

}
