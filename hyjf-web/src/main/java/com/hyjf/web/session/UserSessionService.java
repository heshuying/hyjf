/**
 * Description:获取用户信息接口
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月18日 上午11:42:50
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.web.session;

import com.hyjf.web.BaseService;

/**
 * @author 郭勇
 */

public interface UserSessionService extends BaseService {
	/*
	 *判断用户是否存在，存在true，不存在false
	 */
	public boolean checkUserExists(Integer userId);
}

	