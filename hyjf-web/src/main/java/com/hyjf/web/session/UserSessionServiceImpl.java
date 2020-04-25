/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月18日 上午11:46:33
 * Modification History:
 * Modified by : 
 */

package com.hyjf.web.session;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.web.BaseServiceImpl;

/**
 * @author 郭勇
 */
@Service
public class UserSessionServiceImpl extends BaseServiceImpl implements UserSessionService {

	/**
	 * 根据用户id获取用户信息
	 * 
	 * @param userId
	 * @return
	 * @author 郭勇
	 */

	@Override
	public boolean checkUserExists(Integer userId) {
		Users user = usersMapper.selectByPrimaryKey(userId);
		if (user != null) {
			return true;
		}
		return false;

	}

}
