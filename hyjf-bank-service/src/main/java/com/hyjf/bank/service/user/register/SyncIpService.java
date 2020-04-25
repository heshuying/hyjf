package com.hyjf.bank.service.user.register;

import java.util.Map;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;

/**
 * 用户注册Service实现类
 * 
 * @author xiaojohn
 *
 */
public interface SyncIpService extends BaseService {

	UsersInfo getIpInfo(Users users);

	int updateIpInfo(Users users, UsersInfo userInfo);
	
	
}
