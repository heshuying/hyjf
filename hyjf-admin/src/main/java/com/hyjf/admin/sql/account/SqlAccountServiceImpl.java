package com.hyjf.admin.sql.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.mybatis.mapper.customize.SqlAccountCustomizeMapper;
import com.hyjf.mybatis.model.customize.SqlAccountCustomize;

@Service
public class SqlAccountServiceImpl implements SqlAccountService{
	
	@Autowired
	protected SqlAccountCustomizeMapper sqlAccountCustomizeMapper;

	@Override
	public List<SqlAccountCustomize> selectAccountToday() {
		List<SqlAccountCustomize> list = sqlAccountCustomizeMapper.selectAccountToday();
		return list;
	}
	
	
	

}
