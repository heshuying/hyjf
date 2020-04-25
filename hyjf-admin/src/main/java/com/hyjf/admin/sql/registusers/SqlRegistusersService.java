package com.hyjf.admin.sql.registusers;

import java.util.List;

import com.hyjf.mybatis.model.customize.SqlRegistUsersCustomize;

public interface SqlRegistusersService {
	
	public List<SqlRegistUsersCustomize> selectRegistUsersByRegtime(int date);
}
