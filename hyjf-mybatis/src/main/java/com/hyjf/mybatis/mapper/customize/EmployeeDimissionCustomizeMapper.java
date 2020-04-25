package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.EmployeeDimissionCustomize;

public interface EmployeeDimissionCustomizeMapper {
	public List<EmployeeDimissionCustomize> selectDimission(EmployeeDimissionCustomize employeeDimissionCustomize);
	
	public List<EmployeeDimissionCustomize> selectDimissionDetail(int id);

	int countEmployee();
	
	public void approvalStaff(int id);
}
