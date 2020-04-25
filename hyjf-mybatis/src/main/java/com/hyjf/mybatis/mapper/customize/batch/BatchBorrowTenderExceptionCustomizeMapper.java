package com.hyjf.mybatis.mapper.customize.batch;

import java.util.List;

import com.hyjf.mybatis.model.customize.batch.BatchBorrowTenderCustomize;

/**
 * 
 * @author cwyang
 * 出借掉单异常处理类
 *
 */
public interface BatchBorrowTenderExceptionCustomizeMapper {

	/**
	 * 查询掉单的出借记录
	 * @return
	 */
	public List<BatchBorrowTenderCustomize> queryAuthCodeBorrowTenderList();
	
}
