package com.hyjf.admin.sql.account;

import java.util.List;

import com.hyjf.mybatis.model.customize.SqlAccountCustomize;

public interface SqlAccountService {
	
	public List<SqlAccountCustomize> selectAccountToday();
}
