package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.auto.Admin;
import com.hyjf.mybatis.model.customize.AdminSystem;

public interface AdminSystemMapper {

	/**
	 * 获取用户的左侧菜单
	 * 
	 * @param adminSystem
	 * @return
	 */
	List<AdminSystem> selectLeftMenuTree(AdminSystem adminSystem);

	/**
	 * 获取用户的菜单权限
	 * 
	 * @param adminSystem
	 * @return
	 */
	List<AdminSystem> getUserPermission(AdminSystem adminSystem);

	/**
	 * 获取用户的基本信息
	 * 
	 * @param adminSystem
	 * @return
	 */
	AdminSystem getUserInfo(AdminSystem adminSystem);
	/**
	 * 更新密码
	 * @param adminSystem
	 * @return
	 */
	Integer updatePassword(AdminSystem adminSystem);
	
	/**
	 * 验证登陆时密码是否正确
	 */
	List<Admin> selectByUsername(String username);
}