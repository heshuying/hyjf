package com.hyjf.admin.sql.borrowtender;

import java.util.List;

import com.hyjf.mybatis.model.customize.SqlBorrowTenderCustomize;

public interface BorrowTenderService {
	
	public List<SqlBorrowTenderCustomize> selectBorrowTenderByAddtime(int date);
}
