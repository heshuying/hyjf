package com.hyjf.wrb.regiester;

import com.hyjf.mybatis.model.auto.BindUsers;
import com.hyjf.mybatis.model.auto.UsersInfo;

public interface WrbRegiesterServcie {

	/**
     * 根据用户ID查询用户信息
     * @param userId 用户ID
     * @return
     */
	UsersInfo getUserInfoByUserId(Integer userId);

	/**
	 * 更新用户信息
	 * @param userInfo
	 */
	void updateUserInfo(UsersInfo userInfo);

    /**
     * 查询绑定用户
     * @param userId
     * @return
     */
    BindUsers selectByUserId(Integer userId, String instCode);

    /**
     * 插入绑定用户
     * @param bindUser
     */
    void insertBindUser(BindUsers bindUser);

    /**
     * 用户绑定
     * @param bindUser
     */
	void insertSelective(BindUsers bindUser);
}
