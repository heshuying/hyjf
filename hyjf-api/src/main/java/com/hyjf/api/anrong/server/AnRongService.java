package com.hyjf.api.anrong.server;
import java.util.List;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.UsersInfo;


public interface AnRongService extends BaseService{

	/** 根据id获取用户信息*/
	public List<UsersInfo> getUser(Integer userId);

}
