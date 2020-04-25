package com.hyjf.admin.sql.borrowtender;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.mybatis.mapper.customize.SqlBorrowTenderCustomizeMapper;
import com.hyjf.mybatis.model.customize.SqlBorrowTenderCustomize;

@Service
public class BorrowTenderServiceImpl implements BorrowTenderService{
	
	@Autowired
	protected SqlBorrowTenderCustomizeMapper sqlBorrowTenderCustomizeMapper;
	
	@Override
	public List<SqlBorrowTenderCustomize> selectBorrowTenderByAddtime(int date) {
		List<SqlBorrowTenderCustomize> list = sqlBorrowTenderCustomizeMapper.selectBorrowTenderByAddtime(date);
		return list;
	}
	
	
	

}
