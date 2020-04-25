package com.hyjf.admin.sql.borrowrecover;

import java.util.List;

import com.hyjf.mybatis.model.customize.SqlBorrowRecoverCustomize;

public interface SqlBorrowRecoverService {
	
	public List<SqlBorrowRecoverCustomize> selectBorrowRecoverByRecovertime(int date);
}
