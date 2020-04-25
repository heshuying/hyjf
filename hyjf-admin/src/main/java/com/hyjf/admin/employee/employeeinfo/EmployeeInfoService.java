package com.hyjf.admin.employee.employeeinfo;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.EmployeeDimissionCustomize;
import com.hyjf.mybatis.model.customize.EmployeeInfoCustomize;

public interface EmployeeInfoService extends BaseService{
	/**
	 * 所有员工信息
	 * @return
	 */
	public List<EmployeeInfoCustomize> getRecordList(EmployeeInfoCustomize employeeInfoCustomize);

	/**
	 * 员工信息（账户数量）
	 * @return
	 */
	public Integer countEmployee();
	
	/**
	 * 单个员工
	 * @return
	 */
	public EmployeeInfoCustomize getRecord(int id);
//	public List<EmployeeInfoCustomize> getRecord(int id);
	
//	
//	/**
//	 * 根据主键判断账户数据是否存在
//	 * 
//	 * @return
//	 */
//	public boolean isExistsRecord(int id);
//	
	/**
	 * 账户插入
	 * 
	 * @param record
	 */
	public void insertRecord(EmployeeInfoBean bean);
	
	/**
	 * 编辑员工信息
	 * @param employeeInfoCustomize
	 */
	public void editStaff(EmployeeInfoCustomize employeeInfoCustomize);
	public int editStaffById(int id);
	
	
	/**
	 * 账户更新
	 * 
	 * @param record
	 */
	public void updateRecord(EmployeeInfoBean form);
	
	/**
	 * 账户删除
	 * 
	 * @param ids
	 */
	public void deleteRecord(List<Integer> ids);
	
	/**
	 * 重置密码，123456
	 * @param id
	 */
	public void resetPassword(int id);
	
	
	/**
	 * 员工详细
	 */
	public List<EmployeeInfoCustomize> selectDetailStaff(int id);
	
	/**
	 * 将users表的员工状态改为Q1
	 * @param id
	 */
	public void leaveStaff(int id);
	
	/**
	 * 添加user_leave表的内容
	 */
	public void insertLeaveStaff(EmployeeDimissionCustomize employeeDimissionCustomize);
	
}
