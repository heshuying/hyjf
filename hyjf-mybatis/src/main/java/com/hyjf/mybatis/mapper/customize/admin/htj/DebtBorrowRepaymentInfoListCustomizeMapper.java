package com.hyjf.mybatis.mapper.customize.admin.htj;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowRepaymentInfoListCustomize;

public interface DebtBorrowRepaymentInfoListCustomizeMapper {

	/**
	 * 出借明细详情列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	List<DebtBorrowRepaymentInfoListCustomize> selectBorrowRepaymentInfoListList(DebtBorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize);

	/**
	 * 出借明细详情列表 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	Long countBorrowRepaymentInfoList(DebtBorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize);

	/**
	 * sum出借明细详情列表
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	DebtBorrowRepaymentInfoListCustomize sumBorrowRepaymentInfoList(DebtBorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize);

}