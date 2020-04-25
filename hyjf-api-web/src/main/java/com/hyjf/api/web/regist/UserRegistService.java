package com.hyjf.api.web.regist;

import com.hyjf.api.web.BaseService;

public interface UserRegistService extends BaseService {

	int countUserByMobile(String param);
	
	int countUserByEmail(String param);
	
	String insertRegistUser(UserRegistBean paramBean) throws Exception;
	
	boolean checkUsernameExists(UserRegistBean paramBean);
	
}
