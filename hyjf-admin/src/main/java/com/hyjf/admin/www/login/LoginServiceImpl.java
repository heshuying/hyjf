package com.hyjf.admin.www.login;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.loginerror.config.LoginErrorConfigManager;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.mybatis.model.auto.Admin;
import com.hyjf.mybatis.model.customize.AdminSystem;

@Service
public class LoginServiceImpl extends BaseServiceImpl implements LoginService {
	
	@Override
	public AdminSystem getUsersByUserNameAndPassword(String username, String password) {
		password = MD5.toMD5Code(password);
		AdminSystem adminSystem = new AdminSystem();
		adminSystem.setUsername(username);
		adminSystem.setPassword(password);
		return adminSystemMapper.getUserInfo(adminSystem);
	}

	@Override
	public Boolean updatePassword(String username, String password){
		password = MD5.toMD5Code(password);
		AdminSystem adminSystem = new AdminSystem();
		adminSystem.setUsername(username);
		adminSystem= adminSystemMapper.getUserInfo(adminSystem);
		adminSystem.setPassword(password);
		adminSystemMapper.updatePassword(adminSystem);
		return true;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param username
	 * @return
	 * @author yj
	 */
		
	@Override
	public List<Admin> getUsersByUsername(String username) {
		List<Admin> adminSystems = adminSystemMapper.selectByUsername(username);
		return adminSystems;
			
	}
	/**
	 * redis增加
	 * @param key
	 */
	@Override
	public long insertPassWordCount(String key) {
		long retValue  = RedisUtils.incr(key);
//		RedisUtils.expire(key,RedisUtils.getRemainMiao());//给key设置过期时间
		Integer loginErrorConfigManager=LoginErrorConfigManager.getInstance().getAdminConfig().getLockLong();
		RedisUtils.expire(key,loginErrorConfigManager*3600);//给key设置过期时间
		return retValue;
	}


}
