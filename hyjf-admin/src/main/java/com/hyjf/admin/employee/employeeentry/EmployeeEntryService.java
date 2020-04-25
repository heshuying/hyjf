package com.hyjf.admin.employee.employeeentry;

import java.util.List;

import com.hyjf.mybatis.model.customize.EmployeeEntryCustomize;

public interface EmployeeEntryService {
	public List<EmployeeEntryCustomize> selectEntry(EmployeeEntryCustomize employeeEntryCustomize);
	
	/**
	 * 员工信息（账户数量）
	 * @return
	 */
	public Integer countEmployee();
	
	public List<EmployeeEntryCustomize> selectEntryDetail(int id);
	
	public void approvalStaff(int id);
	
}
