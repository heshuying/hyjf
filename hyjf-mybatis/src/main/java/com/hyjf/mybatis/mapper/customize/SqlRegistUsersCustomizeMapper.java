package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.SqlRegistUsersCustomize;

public interface SqlRegistUsersCustomizeMapper {
	
	public List<SqlRegistUsersCustomize> selectRegistUsersByRegtime(int date);

}
