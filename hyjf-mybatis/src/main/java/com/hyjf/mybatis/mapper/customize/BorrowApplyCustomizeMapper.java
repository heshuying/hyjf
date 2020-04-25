package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.BorrowApplyCustomize;

public interface BorrowApplyCustomizeMapper {

	/**
	 * 放款明细列表
	 * 
	 * @param borrowApplyCustomize
	 * @return
	 */
	List<BorrowApplyCustomize> selectBorrowApplyList(BorrowApplyCustomize borrowApplyCustomize);

	/**
	 * 放款明细记录 总数COUNT
	 * 
	 * @param borrowApplyCustomize
	 * @return
	 */
	Long countBorrowApply(BorrowApplyCustomize borrowApplyCustomize);

}