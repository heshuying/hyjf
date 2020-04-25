package com.hyjf.web.activity.activity518;

import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.ActdecFinancingCustomizeExample;
import com.hyjf.web.BaseServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class Act518ServiceImpl extends BaseServiceImpl implements Act518Service{
    
	Logger _log = LoggerFactory.getLogger(Act518ServiceImpl.class);
	private static final String THIS_CLASS = Act518ServiceImpl.class.getName();
	
	
    /**
     * 获取活动时间
     * 
     * @return
     */
	@Override
    public ActivityList getActivityDate(int id) {
    	
        return activityListMapper.selectByPrimaryKey(id);
    }

	@Override
	public List<UsersInfo> getUserInfo(Integer userId) {
		//获取用户信息
		 UsersInfoExample example = new UsersInfoExample();
				 example.or().andUserIdEqualTo(userId);
		return usersInfoMapper.selectByExample(example);
	}

	

	@Override
	public List<Users> getUser(Integer userId) {
		//获取用户信息
		 UsersExample example = new UsersExample();
				 example.or().andUserIdEqualTo(userId);
		return usersMapper.selectByExample(example);
	}

	@Override
	public List<ActdecFinancing> getAct518(String userId) {
		ActdecFinancingCustomizeExample example=new ActdecFinancingCustomizeExample();
		example.or().andUserIdEqualTo(userId);
		
		return actdecFinancingCustomizeMapper.selectByExample(example);
	}

	@Override
	public int insertAct(ActdecFinancing af) {
		actdecFinancingMapper.insert(af);
		return 0;
	}

	@Override
	public List<ActdecFinancing> getAct518two(Integer userId) {
		return null;
	}

	



}
