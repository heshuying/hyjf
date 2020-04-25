package com.hyjf.app.activity.newyear2018;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.auto.ActdecSpringListExample.Criteria;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@Service
public class SpringActServiceImpl extends BaseServiceImpl implements SpringActService{
    
	SimpleDateFormat df = new SimpleDateFormat("dd");

	
	
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
	public boolean addActdecSpringList(ActdecSpringList actdecSpringList) {
		if(actdecSpringList.getOperType()>5) {
			ActdecSpringListExample example=new ActdecSpringListExample();
			Criteria criteria = example.createCriteria();
			criteria.andUserIdEqualTo(actdecSpringList.getUserId());
			criteria.andOperTypeEqualTo(actdecSpringList.getOperType());
			List<ActdecSpringList> act = actdecSpringListMapper.selectByExample(example);
			if(!act.isEmpty()) {
				return false;
			}
		}

		actdecSpringListMapper.insert(actdecSpringList);
		return true;
	}

	@Override
	public ActdecSpringList getActdecSpringList(String userId) {
		ActdecSpringListExample example=new ActdecSpringListExample();
		example.or().andUserIdEqualTo(userId);
		example.setOrderByClause(" id desc ");
		List<ActdecSpringList> act = actdecSpringListMapper.selectByExample(example);
		if(!act.isEmpty()) {
			return act.get(0);
		}else {
			return null;
		}
	}

	@Override
	public List<ActdecSpringList> getActdecSpringLists(String userId) {
		List<Integer> opers=new ArrayList<>();
		opers.add(1);
		opers.add(2);
		opers.add(3);
		opers.add(4);
		ActdecSpringListExample example=new ActdecSpringListExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andOperTypeIn(opers);
		example.setOrderByClause(" Oper_Type ");
		return actdecSpringListMapper.selectByExample(example);
	}

	@Override
	public List<ActdecSpringList> getZhongJiang(String userId) {
		
		List<Integer> opers=new ArrayList<>();
		opers.add(5);
		opers.add(6);
		opers.add(7);
		opers.add(8);
		opers.add(9);
		//10
		ActdecSpringListExample example=new ActdecSpringListExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andOperTypeIn(opers);
		example.setOrderByClause(" id ");
		return actdecSpringListMapper.selectByExample(example);
	}

	@Override
	public List<Users> getUser(Integer userId) {
		//获取用户信息
		 UsersExample example = new UsersExample();
				 example.or().andUserIdEqualTo(userId);
		return usersMapper.selectByExample(example);
	}

	@Override
	public Account getAccount(int userId) {
		AccountExample example = new AccountExample();
		 example.or().andUserIdEqualTo(userId);
		return accountMapper.selectByExample(example).get(0);
	}





}
