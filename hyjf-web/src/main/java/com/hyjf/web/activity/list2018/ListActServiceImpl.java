package com.hyjf.web.activity.list2018;



import com.hyjf.web.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;


@Service
public class ListActServiceImpl extends BaseServiceImpl implements ListActService{
    
	SimpleDateFormat df = new SimpleDateFormat("dd");
	Logger _log = LoggerFactory.getLogger(ListActServiceImpl.class);
	private static final String THIS_CLASS = ListActServiceImpl.class.getName();
	
	
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
	public ActdecListedThree getAct3(Integer userId) {
		ActdecListedThreeExample example = new ActdecListedThreeExample();
		 example.or().andUserIdEqualTo(userId);
		return actdecListedThreeMapper.selectByExample(example).get(0);
	}

	@Override
	public ActdecListedFour getAct4(Integer userId) {
		ActdecListedFourExample example = new ActdecListedFourExample();
		 example.or().andUserIdEqualTo(userId);
		return actdecListedFourMapper.selectByExample(example).get(0);
	}

	@Override
	public int insertAct3(ActdecListedThree actdecListedThree) {
		
		return actdecListedThreeMapper.insert(actdecListedThree);
	}

	@Override
	public int insertAct4(ActdecListedFour actdecListedFour) {
		return actdecListedFourMapper.insert(actdecListedFour);
	}

	@Override
	public int updateAct3(ActdecListedThree actdecListedThree) {
		
		return actdecListedThreeMapper.updateByPrimaryKey(actdecListedThree);
	}

	@Override
	public int updateAct4(ActdecListedFour actdecListedFour) {
		return actdecListedFourMapper.updateByPrimaryKey(actdecListedFour);
	}

	@Override
	public int getSpreads(Integer userId) {
		SpreadsUsersExample example = new SpreadsUsersExample();
		 example.or().andUserIdEqualTo(userId);
		return spreadsUsersMapper.selectByExample(example).get(0).getSpreadsUserid();
	}

	@Override
	public List<ActdecListedThree> getAct3List( int type) {
		ActdecListedThreeExample example = new ActdecListedThreeExample();
		if(type==1) {
			example.setOrderByClause("single desc,create_time limit 100");
		}else {
			example.setOrderByClause(" cumulative desc,create_time limit 100");
		}
		
		return actdecListedThreeMapper.selectByExample(example);
	}

	@Override
	public List<ActdecListedFour> getAct4List( int type) {
		if(type==1) {
			ActdecListedFourExample example = new ActdecListedFourExample();
			example.setOrderByClause(" cumulative desc,create_time limit 100");
			return actdecListedFourMapper.selectByExample(example);
		}else if(type==2){
			return listActServiceCustomizeMapper.selectActdecListed();
		}else if(type==3) {
			return listActServiceCustomizeMapper.selectActdecListedTwo();
		}
			
		return null;
	}

	@Override
	public List<ActdecListedOne> getAct1List(Integer userId) {
		ActdecListedOneExample example = new ActdecListedOneExample();
		 com.hyjf.mybatis.model.auto.ActdecListedOneExample.Criteria criteria = example.createCriteria();
		 criteria.andUserIdEqualTo(userId);
		 criteria.andWhetherEqualTo(1);
		 example.setOrderByClause(" update_time asc ");
		return actdecListedOneMapper.selectByExample(example);
	}

	@Override
	public ActdecListedOne getAct1(Integer userId) {
		ActdecListedOneExample example = new ActdecListedOneExample();
		 com.hyjf.mybatis.model.auto.ActdecListedOneExample.Criteria criteria = example.createCriteria();
		 criteria.andUserIdEqualTo(userId);
		 criteria.andWhetherEqualTo(0);
		 example.setOrderByClause(" update_time asc ");
		 List<ActdecListedOne> as = actdecListedOneMapper.selectByExample(example);
		 if(as.isEmpty()) {
			 return null; 
		 }
		return as.get(0);
	}

	@Override
	public int insertAct1(ActdecListedOne one) {
		return actdecListedOneMapper.updateByPrimaryKey(one);
	}

	@Override
	public List<BorrowTender> getBorrowTender(String orderId) {
		BorrowTenderExample example = new BorrowTenderExample();
		example.or().andBorrowNidEqualTo(orderId);
		example.setOrderByClause(" id asc ");
		return borrowTenderMapper.selectByExample(example);
	}

	@Override
	public Borrow getBorrow(String borrowNid) {
		BorrowExample example = new BorrowExample();
		example.or().andBorrowNidEqualTo(borrowNid);
		return borrowMapper.selectByExample(example).get(0);
	}




}
