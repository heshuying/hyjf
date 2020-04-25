package com.hyjf.admin.www.login;

import java.util.List;

import com.hyjf.mybatis.model.auto.Admin;
import com.hyjf.mybatis.model.customize.AdminSystem;

public interface LoginService {
	/**
	 * 根据用户名和密码获取用户信息(没加密过的密码)
	 * @param username
	 * @param password
	 * @return
	 */
	public AdminSystem getUsersByUserNameAndPassword(String username,String password);
	/**
	 * 根据用户名和密码获取用户信息(没加密过的密码)
	 * @param username
	 * @param password
	 * @return
	 */
	public Boolean updatePassword(String username,String password);
	
	/**
	 * 根据用户名判断是否有该用户，从而判断密码是否正确
	 */
	public List<Admin> getUsersByUsername(String username);
	
	/**
	 * redis增加用户登录密码输入错误次数
	 */
	public long insertPassWordCount(String key);
}
