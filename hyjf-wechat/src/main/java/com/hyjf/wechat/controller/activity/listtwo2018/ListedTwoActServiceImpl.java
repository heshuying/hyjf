package com.hyjf.wechat.controller.activity.listtwo2018;

import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.ActdecListedTwo;
import com.hyjf.mybatis.model.auto.ActdecListedTwoExample;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.wechat.base.BaseServiceImpl;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ListedTwoActServiceImpl extends BaseServiceImpl implements ListedTwoActService{
    
    /**
     * 获取活动时间
     * 
     * @return
     */
	@Override
    public ActivityList getActivityDate(int id) {
        return activityListMapper.selectByPrimaryKey(id);
    }

	/**
	 * 获取用户详细信息
	 * 
	 * @param userId
	 * @return
	 * @author PC-LIUSHOUYI
	 */
	@Override
	public List<UsersInfo> getUserInfo(Integer userId) {
		//获取用户信息
		 UsersInfoExample example = new UsersInfoExample();
				 example.or().andUserIdEqualTo(userId);
		return usersInfoMapper.selectByExample(example);
	}

	/**
	 * 操作记录入库（充值、出借、提现、领奖）
	 * 
	 * @param actdecListedTwo
	 * @return
	 * @author PC-LIUSHOUYI
	 */
	@Override
	public boolean addActdecSpringList(ActdecListedTwo actdecListedTwo) {
		return actdecListedTwoMapper.insert(actdecListedTwo) > 0 ? true : false;
	}

	/**
	 * 获取最新记录
	 * 
	 * @param userId
	 * @return
	 * @author PC-LIUSHOUYI
	 */
	@Override
	public ActdecListedTwo getActdecListedTwoList(String userId) {
		ActdecListedTwoExample example=new ActdecListedTwoExample();
		example.or().andUserIdEqualTo(Integer.valueOf(userId));
		example.setOrderByClause(" create_time desc ");
		List<ActdecListedTwo> act = actdecListedTwoMapper.selectByExample(example);
		if(!act.isEmpty()) {
			return act.get(0);
		}else {
			return null;
		}
	}

	/**
	 * 获取用户信息
	 * 
	 * @param userId
	 * @return
	 * @author PC-LIUSHOUYI
	 */
	@Override
	public List<Users> getUser(Integer userId) {
		//获取用户信息
		UsersExample example = new UsersExample();
				 example.or().andUserIdEqualTo(userId);
		return usersMapper.selectByExample(example);
	}

	/**
	 * 获取账户信息
	 * 
	 * @param userId
	 * @return
	 * @author PC-LIUSHOUYI
	 */
	@Override
	public Account getAccount(int userId) {
		AccountExample example = new AccountExample();
		 example.or().andUserIdEqualTo(userId);
		return accountMapper.selectByExample(example).get(0);
	}
}
