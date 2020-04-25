package com.hyjf.server.module.wkcd.user.regist;

import com.hyjf.mybatis.model.auto.Users;

public interface WkcdRegistService {
	/**
	 * 根据手机号获取用户信息
	 * @param mobile
	 * @return
	 */
	public boolean existUser(String mobile);
	/**
	 * 根据手机号获取用户信息
	 * @param mobile
	 * @return
	 */
	public Users getUserByMobile(String mobile);
	
	/**
	 * 注册用户
	 * @param mobile
	 * @param password
	 * @return
	 */
	public int registUser(String mobile,String password, Users returnUser);
	
	
}



