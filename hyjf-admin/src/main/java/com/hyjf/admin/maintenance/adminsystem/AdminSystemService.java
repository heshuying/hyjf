package com.hyjf.admin.maintenance.adminsystem;

import java.util.List;

import com.hyjf.mybatis.model.customize.AdminSystem;

public interface AdminSystemService {

	public List<AdminSystem> selectLeftMenuTree(AdminSystem adminSystem);

	public List<Integer> selectAdminGroup(AdminSystem adminSystem);

	public List<AdminSystem> getUserPermission(AdminSystem adminSystem);

	public AdminSystem getUserInfo(AdminSystem adminSystem);

}
