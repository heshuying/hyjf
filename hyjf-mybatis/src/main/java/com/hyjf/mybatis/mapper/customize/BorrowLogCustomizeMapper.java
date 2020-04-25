package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.BorrowLogCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowLogCustomize;

public interface BorrowLogCustomizeMapper {

	/**
	 * 获取借款列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	List<BorrowLogCustomize> selectBorrowLogList(BorrowLogCommonCustomize borrowLogCustomize);
	
	/**
	 * 获取借款列表
	 * @param borrowCommonCustomize
	 * @return
	 */
	List<BorrowLogCustomize> searchBorrowLogList(BorrowLogCommonCustomize borrowLogCustomize);

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	Long countBorrowLog(BorrowLogCommonCustomize borrowLogCustomize);


	/**
	 * 列表导出
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	List<BorrowLogCustomize> exportBorrowLogList(BorrowLogCommonCustomize borrowCommonCustomize);

}