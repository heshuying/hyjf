package com.hyjf.admin.sql.withdraw;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.mybatis.mapper.customize.SqlWithdrawCustomizeMapper;
import com.hyjf.mybatis.model.customize.SqlWithdrawCustomize;

@Service
public class SqlWithdrawServiceImpl implements SqlWithdrawService{
	
	@Autowired
	protected SqlWithdrawCustomizeMapper sqlWithdrawCustomizeMapper;
	
	@Override
	public List<SqlWithdrawCustomize> selectWithdrawByAddtime(int date) {
		List<SqlWithdrawCustomize> list = sqlWithdrawCustomizeMapper.selectWithdrawByAddtime(date);
		return list;
	}
	
	
	

}
