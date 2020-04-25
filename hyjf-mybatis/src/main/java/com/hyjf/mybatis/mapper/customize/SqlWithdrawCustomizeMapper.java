package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.SqlWithdrawCustomize;

public interface SqlWithdrawCustomizeMapper {
	
	public List<SqlWithdrawCustomize> selectWithdrawByAddtime(int date);

}
