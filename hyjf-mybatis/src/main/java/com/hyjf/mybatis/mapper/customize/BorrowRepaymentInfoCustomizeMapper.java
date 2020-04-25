package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.BorrowRepaymentInfoCustomize;
import com.hyjf.mybatis.model.customize.apiweb.ApiBorrowRepaymentInfoCustomize;
import com.hyjf.mybatis.model.customize.apiweb.ApiBorrowRepaymentInfoCustomizeRe;

public interface BorrowRepaymentInfoCustomizeMapper {

	/**
	 * 出借明细列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	List<BorrowRepaymentInfoCustomize> selectBorrowRepaymentInfoList(
			BorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize);
	
	/**
     * 出借明细列表--外部api调用
     * 
     * @param alllBorrowCustomize
     * @return
     */
	 List<ApiBorrowRepaymentInfoCustomizeRe> apiSearchBorrowRepaymentInfoList(ApiBorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize);

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return   
	 */
	Long countBorrowRepaymentInfo(BorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize);

	/**
	 * sum出借明细记录 总数
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	BorrowRepaymentInfoCustomize sumBorrowRepaymentInfo(BorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize);
	
	/**
	 * 还款明细列表查询(不牵扯CRM)
	 * 
	 * @param BorrowRepaymentInfoCustomize
	 * @return
	 */
	List<BorrowRepaymentInfoCustomize> selectBorrowRepaymentInfoListForView(BorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize);

}