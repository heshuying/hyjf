package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.auto.ExceptionAccount;

public interface ExceptionAccountCustomizeMapper {
	
	/**
	 * 查询相应的数据总量
	 * @return
	 */
	public long countAllUserAccount();

	/**
	 * 查询相应的数据
	 * @param page
	 * @param limitEnd
	 * @return
	 */
	public List<ExceptionAccount> selectUserAccounts(@Param("limitStart")long limitStart, @Param("limit")long limit);

}
