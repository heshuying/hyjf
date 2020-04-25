package com.hyjf.web.user.findpassword;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.web.BaseServiceImpl;

@Service
public class FindPasswordServiceImpl extends BaseServiceImpl implements FindPasswordService {

	@Override
	public Boolean existPhone(String phone) {
		UsersExample example1 = new UsersExample();
		example1.createCriteria().andMobileEqualTo(phone);
		int size = usersMapper.countByExample(example1);
		if (size > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 返回-1:没有查到用户,-2:存在重复的电话号码,0代表未做任何操作,1代表成功
	 */
	@Override
	public Integer updatePassword(String phone, String password) {
		UsersExample example = new UsersExample();
		example.createCriteria().andMobileEqualTo(phone);
		List<Users> userList = usersMapper.selectByExample(example);
		if (userList == null || userList.size() == 0) {
			return -1;
		} else {
			if (userList.size() > 1) {
				return -2;
			} else {
				Users user = userList.get(0);
				user.setPassword(MD5Utils.MD5(MD5Utils.MD5(password) + user.getSalt()));
				int result = usersMapper.updateByPrimaryKeySelective(user);
				return result;
			}
		}
	}

}
