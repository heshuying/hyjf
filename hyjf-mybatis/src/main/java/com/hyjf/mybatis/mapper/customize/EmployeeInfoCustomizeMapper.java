package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.EmployeeDimissionCustomize;
import com.hyjf.mybatis.model.customize.EmployeeEntryCustomize;
import com.hyjf.mybatis.model.customize.EmployeeInfoCustomize;

public interface EmployeeInfoCustomizeMapper {
	/**
	 * 所有员工信息
	 * @return
	 */
	public List<EmployeeInfoCustomize> selectStaff(EmployeeInfoCustomize employeeInfoCustomize);
	
	int countEmployee();
	
	public List<EmployeeInfoCustomize> selectDetail(int id);
	
	public void editStaff(EmployeeInfoCustomize employeeInfoCustomize);
	
	public void updatePassword(int id);
	
	public Integer updateByUserid(int id);
	
	 int insert(EmployeeInfoCustomize employeeInfoCustomize);
	 
	public void insert1(EmployeeInfoCustomize employeeInfoCustomize);
	
	public void leaveStaff(int id);
	
	public void insertLeaveStaff(EmployeeDimissionCustomize employeeDimissionCustomize);
	
	public void insertEntry(EmployeeEntryCustomize employeeEntryCustomize);
}
