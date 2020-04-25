package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.EmployeeEntryCustomize;

public interface EmployeeEntryCustomizeMapper {
	/**
	 * 入职员工列表
	 * @return
	 */
	public List<EmployeeEntryCustomize> selectEntry(EmployeeEntryCustomize employeeEntryCustomize);
	
	int countEmployee();
	
	public List<EmployeeEntryCustomize> selectEntryDetail(int id);
	
	/**
	 * 审批入职
	 * @param id
	 */
	public void approvalStaff(int id);

}
