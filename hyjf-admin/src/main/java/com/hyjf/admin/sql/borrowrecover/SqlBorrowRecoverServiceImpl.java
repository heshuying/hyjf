package com.hyjf.admin.sql.borrowrecover;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.mybatis.mapper.customize.SqlBorrowRecoverCustomizeMapper;
import com.hyjf.mybatis.model.customize.SqlBorrowRecoverCustomize;

@Service
public class SqlBorrowRecoverServiceImpl implements SqlBorrowRecoverService{
	
	@Autowired
	protected SqlBorrowRecoverCustomizeMapper sqlBorrowRecoverCustomizeMapper ;

	@Override
	public List<SqlBorrowRecoverCustomize> selectBorrowRecoverByRecovertime(int date) {
		List<SqlBorrowRecoverCustomize> list = sqlBorrowRecoverCustomizeMapper.selectBorrowRecoverByRecovertime(date);
		return list;
	}
	
	
	

}
