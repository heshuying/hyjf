package com.hyjf.admin.sql.registusers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.mybatis.mapper.customize.SqlRegistUsersCustomizeMapper;
import com.hyjf.mybatis.model.customize.SqlRegistUsersCustomize;

@Service
public class SqlRegistusersServiceImpl implements SqlRegistusersService{
	
	@Autowired
	protected  SqlRegistUsersCustomizeMapper sqlRegistUsersCustomizeMapper;

	@Override
	public List<SqlRegistUsersCustomize> selectRegistUsersByRegtime(int date) {
		List<SqlRegistUsersCustomize> list = sqlRegistUsersCustomizeMapper.selectRegistUsersByRegtime(date);
		return list;
	}
	
	
	

}
