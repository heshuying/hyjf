package com.hyjf.admin.maintenance.permissions;

import java.util.List;

import com.hyjf.mybatis.model.auto.AdminPermissions;

public interface AdminPermissionsService {

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<AdminPermissions> getRecordList(AdminPermissionsBean AdminPermissions, int limitStart, int limitEnd);

	/**
	 * 获取单个权限维护
	 * 
	 * @return
	 */
	public AdminPermissions getRecord(AdminPermissions record);

	/**
	 * 根据主键判断权限维护中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(AdminPermissions record);

	/**
	 * 根据主键判断权限维护中权限是否存在
	 * 
	 * @return
	 */
	public boolean isExistsPermission(AdminPermissions record);

	/**
	 * 权限维护插入
	 * 
	 * @param record
	 */
	public void insertRecord(AdminPermissions record);

	/**
	 * 权限维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(AdminPermissions record);

	/**
	 * 权限维护删除
	 * 
	 * @param record
	 */
	public void deleteRecord(List<String> recordList);
}
