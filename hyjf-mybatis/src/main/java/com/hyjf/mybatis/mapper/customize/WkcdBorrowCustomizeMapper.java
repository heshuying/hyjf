package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.WkcdBorrowCustomize;
import com.hyjf.mybatis.model.customize.WkcdOverDateCustomize;

public interface WkcdBorrowCustomizeMapper {
	/**
	 * 查找要发给微可的还款计划 
	 * @return
	 */
	public List<WkcdBorrowCustomize> selectToSend();

	/**
	 * 查询逾期的微可标的
	 * @return
	 */
	public List<WkcdOverDateCustomize> selectOverDate();
}
