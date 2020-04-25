package com.hyjf.admin.employee.employeeentry;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.mybatis.mapper.customize.EmployeeEntryCustomizeMapper;
import com.hyjf.mybatis.model.customize.EmployeeEntryCustomize;

@Service
public class EmployeeEntryServiceImpl implements EmployeeEntryService{
	@Autowired
	protected EmployeeEntryCustomizeMapper employeeEntryCustomizeMapper;

	@Override
	public List<EmployeeEntryCustomize> selectEntry(EmployeeEntryCustomize employeeEntryCustomize) {
		List<EmployeeEntryCustomize> list = employeeEntryCustomizeMapper.selectEntry(employeeEntryCustomize);
		return list;
		
	}

	@Override
	public Integer countEmployee() {
		Integer employeeCount = employeeEntryCustomizeMapper.countEmployee();
		return employeeCount;
	}

	@Override
	public List<EmployeeEntryCustomize> selectEntryDetail(int id) {
		List<EmployeeEntryCustomize> list = employeeEntryCustomizeMapper.selectEntryDetail(id);
		return list;
	}

	@Override
	public void approvalStaff(int id) {
		employeeEntryCustomizeMapper.approvalStaff(id);
		
	}
	
	

}
