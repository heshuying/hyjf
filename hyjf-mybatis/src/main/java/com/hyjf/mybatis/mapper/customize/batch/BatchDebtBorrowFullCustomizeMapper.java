package com.hyjf.mybatis.mapper.customize.batch;

import java.util.List;

import com.hyjf.mybatis.model.auto.DebtBorrowWithBLOBs;
import com.hyjf.mybatis.model.customize.batch.BatchDebtBorrowCommonCustomize;

public interface BatchDebtBorrowFullCustomizeMapper {

	/**
	 * 获取自动复审的记录
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	List<DebtBorrowWithBLOBs> selectAutoFullList(BatchDebtBorrowCommonCustomize borrowCommonCustomize);

}