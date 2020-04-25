package com.hyjf.mybatis.mapper.customize.batch;

import org.apache.ibatis.annotations.Param;


public interface BorrowRepayCustomizeMapper {

	Integer getRepayUserId(@Param("borrowUserId") Integer borrowUserId,@Param("borrowNid") String borrowNid);
	
}