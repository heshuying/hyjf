package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.BorrowRepaymentInfoListCustomize;

public interface BorrowRepaymentInfoListCustomizeMapper {

	/**
	 * 出借明细详情列表（分期）
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	List<BorrowRepaymentInfoListCustomize> selectBorrowRepaymentInfoListList(
			BorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize);
	
	/**
     * 出借明细详情列表
     * 
     * @param alllBorrowCustomize
     * @return
     */
    List<BorrowRepaymentInfoListCustomize> selectBorrowRecoverInfoList(
            BorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize);

	/**
	 * 出借明细详情列表 总数COUNT（分期）
	 * 
	 * @param borrowCustomize 
	 * @return
	 */
	Long countBorrowRepaymentInfoList(BorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize);
	
	/**
     * 出借明细详情列表 总数COUNT
     * 
     * @param borrowCustomize 
     * @return
     */
    Long countBorrowRecoverInfoList(BorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize);

	/**
	 * sum出借明细详情列表（分期）
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	BorrowRepaymentInfoListCustomize sumBorrowRepaymentInfoList(
			BorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize);
	
	/**
     * sum出借明细详情列表
     * 
     * @param borrowCustomize
     * @return
     */
    BorrowRepaymentInfoListCustomize sumBorrowRecoverInfoList(
            BorrowRepaymentInfoListCustomize borrowRepaymentInfoListCustomize);

}