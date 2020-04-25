package com.hyjf.admin.sql.withdraw;

import java.util.List;

import com.hyjf.mybatis.model.customize.SqlWithdrawCustomize;

public interface SqlWithdrawService {
	
	public List<SqlWithdrawCustomize> selectWithdrawByAddtime(int date);
}
