package com.hyjf.admin.employee.employeedimission;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.mybatis.mapper.customize.EmployeeDimissionCustomizeMapper;
import com.hyjf.mybatis.model.customize.EmployeeDimissionCustomize;

@Service
public class EmployeeDimissionServiceImpl implements EmployeeDimissionService{
	@Autowired
	protected EmployeeDimissionCustomizeMapper employeeDimissionCustomizeMapper;
	
	
	@Override
	public List<EmployeeDimissionCustomize> selectDimission(EmployeeDimissionCustomize employeeDimissionCustomize) {
		List<EmployeeDimissionCustomize> list = employeeDimissionCustomizeMapper.selectDimission(employeeDimissionCustomize);
		return list;
	}

	@Override
	public List<EmployeeDimissionCustomize> selectDimissionDetail(int id) {
		List<EmployeeDimissionCustomize> list = employeeDimissionCustomizeMapper.selectDimissionDetail(id);
		return list;
	}

	@Override
	public Integer countEmployee() {
		Integer employeeCount = employeeDimissionCustomizeMapper.countEmployee();
		return employeeCount;
	}

	@Override
	public void approvalStaff(int id) {
		employeeDimissionCustomizeMapper.approvalStaff(id);
		
	}

}
