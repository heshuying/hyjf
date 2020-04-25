package com.hyjf.admin.manager.borrow.borrowlog;

import java.util.List;

import com.hyjf.mybatis.model.customize.BorrowLogCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowLogCustomize;

public interface BorrowLogService {

	/**
	 * 合计
	 * 
	 * @return
	 */
	public Long countBorrowLog(BorrowLogCommonCustomize borrowLogCustomize);


	/**
	 * 借款列表
	 * 
	 * @return
	 */
	public List<BorrowLogCustomize> selectBorrowLogList(BorrowLogCommonCustomize borrowLogCustomize);

	/**
	 * 列表导出
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public List<BorrowLogCustomize> exportBorrowLogList(BorrowLogCommonCustomize borrowLogCustomize);
}
