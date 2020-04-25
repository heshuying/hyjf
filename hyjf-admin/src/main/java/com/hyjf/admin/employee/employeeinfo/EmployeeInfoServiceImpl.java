package com.hyjf.admin.employee.employeeinfo;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.mapper.customize.EmployeeInfoCustomizeMapper;
import com.hyjf.mybatis.model.customize.EmployeeDimissionCustomize;
import com.hyjf.mybatis.model.customize.EmployeeEntryCustomize;
import com.hyjf.mybatis.model.customize.EmployeeInfoCustomize;

@Service
public class EmployeeInfoServiceImpl extends BaseServiceImpl implements EmployeeInfoService{
	@Autowired
	protected EmployeeInfoCustomizeMapper employeeInfoCustomizeMapper;

	@Override
	public List<EmployeeInfoCustomize> getRecordList(EmployeeInfoCustomize employeeInfoCustomize) {
		List<EmployeeInfoCustomize> list = employeeInfoCustomizeMapper.selectStaff(employeeInfoCustomize);
		return list;
	}

	@Override
	public Integer countEmployee() {
		Integer employeeCount = employeeInfoCustomizeMapper.countEmployee();
		return employeeCount;
	}

	@Override
	public EmployeeInfoCustomize getRecord(int id) {
		EmployeeInfoCustomize employeeInfoCustomize = new EmployeeInfoCustomize();
		employeeInfoCustomize.setId(id);
	    List<EmployeeInfoCustomize> list = employeeInfoCustomizeMapper.selectStaff(employeeInfoCustomize);
	    if (list != null && list.size() > 0) {
	        return list.get(0);
	    }
	    return new EmployeeInfoCustomize();
	}
	

	@Override
	public void insertRecord(EmployeeInfoBean form) {
		
        EmployeeInfoCustomize employeeInfoCustomize = new EmployeeInfoCustomize();
        BeanUtils.copyProperties(form, employeeInfoCustomize);

        employeeInfoCustomizeMapper.insert1(form);
        
        EmployeeEntryCustomize employeeEntryCustomize = new EmployeeEntryCustomize();
        
        employeeEntryCustomize.setOtype("0");
        employeeEntryCustomize.setOprocess("E1");
        employeeEntryCustomize.setOstatus("0");
        employeeEntryCustomize.setUserid(form.getId());
        employeeEntryCustomize.setCreater(0);
        employeeEntryCustomize.setRemark("null");
        employeeEntryCustomize.setCreatetime("1");
        
        employeeInfoCustomizeMapper.insertEntry(employeeEntryCustomize);
        
        
	}

	
	@Override
	public void updateRecord(EmployeeInfoBean form) {
        EmployeeInfoCustomize employeeInfoCustomize = new EmployeeInfoCustomize();
        BeanUtils.copyProperties(form, employeeInfoCustomize);

	    employeeInfoCustomizeMapper.editStaff(employeeInfoCustomize);		
	}
	
	/**
	 * 账户删除
	 * @param ids
	 */
	@Override
	public void deleteRecord(List<Integer> ids) {
		
		
	}

	@Override
	public void resetPassword(int id) {
		employeeInfoCustomizeMapper.updatePassword(id);	
		
	}



	@Override
	public void editStaff(EmployeeInfoCustomize employeeInfoCustomize) {
		employeeInfoCustomize.setDepartmentid(employeeInfoCustomize.getDepartmentid());
		employeeInfoCustomize.setLevel(employeeInfoCustomize.getLevel());
		employeeInfoCustomize.setPositionid(employeeInfoCustomize.getPositionid());
		employeeInfoCustomize.setPayroll(employeeInfoCustomize.getPayroll());
		employeeInfoCustomize.setSex(employeeInfoCustomize.getSex());
		employeeInfoCustomize.setAge(employeeInfoCustomize.getAge());
		employeeInfoCustomize.setMobile(employeeInfoCustomize.getMobile());
		employeeInfoCustomize.setAcc_address(employeeInfoCustomize.getAcc_address());
		employeeInfoCustomize.setTemporary(employeeInfoCustomize.getTemporary());
		employeeInfoCustomize.setBank_address(employeeInfoCustomize.getBank_address());
		employeeInfoCustomize.setBank_user(employeeInfoCustomize.getBank_user());
		employeeInfoCustomize.setBank_num(employeeInfoCustomize.getBank_num());
		employeeInfoCustomize.setUser_email(employeeInfoCustomize.getUser_email());
		employeeInfoCustomize.setReference(employeeInfoCustomize.getReference());
		employeeInfoCustomize.setEnterdate(employeeInfoCustomize.getEnterdate());
		employeeInfoCustomize.setEducation(employeeInfoCustomize.getEducation());
		employeeInfoCustomize.setSpecialty(employeeInfoCustomize.getSpecialty());
		employeeInfoCustomizeMapper.editStaff(employeeInfoCustomize);		
	}
	
	@Override
	public int editStaffById(int id) {
		int count = employeeInfoCustomizeMapper.updateByUserid(id);
		return count;
	}
	
	/**
	 * 员工详细信息
	 * @return
	 */
	@Override
	public List<EmployeeInfoCustomize> selectDetailStaff(int id) {
		List<EmployeeInfoCustomize> list = employeeInfoCustomizeMapper.selectDetail(id);
		return list;
	}
	
	public void leaveStaff(int id){
		employeeInfoCustomizeMapper.leaveStaff(id);
	}

	@Override
	public void insertLeaveStaff(EmployeeDimissionCustomize employeeDimissionCustomize) {
//		EmployeeDimissionCustomize record = new EmployeeDimissionCustomize();
//		BeanUtils.copyProperties(form, record);
		employeeDimissionCustomize.setCreater(1);
		employeeDimissionCustomize.setF_creater(0);
		employeeDimissionCustomize.setF_remark(null);
		employeeDimissionCustomize.setF_time(null);
		employeeDimissionCustomize.setS_creater(0);
		employeeDimissionCustomize.setS_remark(null);
		employeeDimissionCustomize.setS_time(null);
		employeeDimissionCustomize.setQ_creater(0);
		employeeDimissionCustomize.setQ_remark(null);
		employeeDimissionCustomize.setQ_time(null);
		employeeDimissionCustomize.setCreatetime(null);
		employeeInfoCustomizeMapper.insertLeaveStaff(employeeDimissionCustomize);
		
	}
}
