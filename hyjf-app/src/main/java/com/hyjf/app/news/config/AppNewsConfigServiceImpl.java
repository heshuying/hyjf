package com.hyjf.app.news.config;

import org.springframework.stereotype.Service;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.Users;
@Service
public class AppNewsConfigServiceImpl extends BaseServiceImpl implements AppNewsConfigService {

	
	/**
	 * 开关闭推送服务 
	 * @param 
	 * @return
	 */
	public int updateAppNewsConfig(Users users){
		return usersMapper.updateByPrimaryKeySelective(users);
	}
	
}




