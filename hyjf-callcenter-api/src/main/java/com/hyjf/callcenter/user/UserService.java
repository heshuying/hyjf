package com.hyjf.callcenter.user;

import com.hyjf.mybatis.model.auto.Users;
/**
 * 查询用户接口类
 * @author 刘彬
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年07月15日 
 */
public interface UserService {
	Users getUser(UserBean bean);
}
