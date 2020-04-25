package com.hyjf.app.sharenews;

import com.hyjf.app.BaseService;

public interface ShareNewsService extends BaseService {

//	/** 根据用户id获取用户信息*/
//	public UsersInfo queryUserInfoById(Integer userId);
	
	/**
	 * 获取分享信息
	 * @return
	 */
	public ShareNewsBean queryShareNews();
}
