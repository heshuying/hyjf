package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.SqlBorrowTenderCustomize;

public interface SqlBorrowTenderCustomizeMapper {
	
	public List<SqlBorrowTenderCustomize> selectBorrowTenderByAddtime(int date);

}
