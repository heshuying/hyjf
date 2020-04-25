package com.hyjf.api.anrong.server;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;

@Service
public class AnRongServiceImpl extends BaseServiceImpl  implements AnRongService {
    
    /**
     * 根据id获取用户信息
     */
    @Override
    public List<UsersInfo> getUser(Integer userId) {
        UsersInfoExample example = new UsersInfoExample();
        UsersInfoExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return usersInfoMapper.selectByExample(example);
    }
    
}
