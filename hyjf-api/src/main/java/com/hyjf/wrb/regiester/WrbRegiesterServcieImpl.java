package com.hyjf.wrb.regiester;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.BindUsers;
import com.hyjf.mybatis.model.auto.BindUsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class WrbRegiesterServcieImpl extends BaseServiceImpl implements WrbRegiesterServcie {

	@Override
	public UsersInfo getUserInfoByUserId(Integer userId) {
		UsersInfo usersInfo = null;
		UsersInfoExample example = new UsersInfoExample();
		UsersInfoExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<UsersInfo> usersInfoList = usersInfoMapper.selectByExample(example);
		if (usersInfoList != null && usersInfoList.size() > 0) {
			usersInfo = usersInfoList.get(0);
		}
		usersInfoMapper.updateByPrimaryKey(usersInfo);
		return usersInfo;
	}

	@Override
	public void updateUserInfo(UsersInfo usersInfo) {
		usersInfoMapper.updateByPrimaryKey(usersInfo);
	}

    @Override
    public BindUsers selectByUserId(Integer userId, String instCode) {
        BindUsersExample example = new BindUsersExample();
        BindUsersExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andBindPlatformIdEqualTo(Integer.valueOf(instCode));
        List<BindUsers> bindUsers = bindUsersMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(bindUsers)) {
            return bindUsers.get(0);
        }
        return null;
    }

    @Override
    public void insertBindUser(BindUsers bindUser) {
        bindUsersMapper.insert(bindUser);
    }

	@Override
	public void insertSelective(BindUsers bindUser) {
		bindUsersMapper.insertSelective(bindUser);
	}

}
