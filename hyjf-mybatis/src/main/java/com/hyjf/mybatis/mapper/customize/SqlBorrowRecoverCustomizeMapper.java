package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.SqlBorrowRecoverCustomize;

public interface SqlBorrowRecoverCustomizeMapper {
	
	public List<SqlBorrowRecoverCustomize> selectBorrowRecoverByRecovertime(int date);
	
}
