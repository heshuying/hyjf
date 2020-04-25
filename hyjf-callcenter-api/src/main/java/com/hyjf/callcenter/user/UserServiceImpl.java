package com.hyjf.callcenter.user;

import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;

/**
 * 查询用户实现类
 * @author 刘彬
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年07月15日 
 */
@Service
public class UserServiceImpl extends CustomizeMapper implements UserService {

	@Override
	public Users getUser(UserBean bean) {
		Users result = null;
        
		UsersExample usersExample = new UsersExample();
		UsersExample.Criteria criteria = usersExample.createCriteria();
		if (StringUtils.isNotBlank(bean.getUserName())) 
			criteria.andUsernameEqualTo(bean.getUserName());
		if (StringUtils.isNotBlank(bean.getMobile())) 
			criteria.andMobileEqualTo(bean.getMobile());
		
		List<Users> usersList = usersMapper.selectByExample(usersExample);
        if (usersList != null && usersList.size() == 1) {
        	//usersList.size() ！= 1的异常处理 TODO
        	return usersList.get(0);
        }
        return result;
	}
}


