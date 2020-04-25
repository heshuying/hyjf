package com.hyjf.admin.employee.employeedimission;

import java.util.List;

import com.hyjf.mybatis.model.customize.EmployeeDimissionCustomize;

public interface EmployeeDimissionService {
	public List<EmployeeDimissionCustomize> selectDimission(EmployeeDimissionCustomize employeeDimissionCustomize);
	
	public List<EmployeeDimissionCustomize> selectDimissionDetail(int id);
	
	/**
	 * 员工信息（账户数量）
	 * @return
	 */
	public Integer countEmployee();
	
	/**
	 * 审批
	 */
	public void approvalStaff(int id);
	

}
