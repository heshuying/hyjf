package com.hyjf.app.news.config;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.Users;

public interface AppNewsConfigService extends BaseService {

//	/** 根据用户id获取用户信息*/
//	public UsersInfo queryUserInfoById(Integer userId);
	
	/**
	 * 开关闭推送服务 
	 * @param 
	 * @return
	 */
	public int updateAppNewsConfig(Users users);
	
}
