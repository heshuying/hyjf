package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.SqlAccountCustomize;

public interface SqlAccountCustomizeMapper {
	
	public List<SqlAccountCustomize> selectAccountToday(); 

}
