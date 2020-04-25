package com.hyjf.mybatis.mapper.customize.admin.htj;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowApplyCustomize;

public interface DebtBorrowApplyCustomizeMapper {

	/**
	 * 放款明细列表
	 * 
	 * @param borrowApplyCustomize
	 * @return
	 */
	List<DebtBorrowApplyCustomize> selectBorrowApplyList(DebtBorrowApplyCustomize debtBorrowApplyCustomize);

	/**
	 * 放款明细记录 总数COUNT
	 * 
	 * @param borrowApplyCustomize
	 * @return
	 */
	Long countBorrowApply(DebtBorrowApplyCustomize debtBorrowApplyCustomize);

}