package com.hyjf.web.activity.list2018;

import java.util.List;

import com.hyjf.web.BaseService;

import com.hyjf.mybatis.model.auto.ActdecListedFour;
import com.hyjf.mybatis.model.auto.ActdecListedOne;
import com.hyjf.mybatis.model.auto.ActdecListedThree;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;


public interface ListActService extends BaseService{


    public ActivityList getActivityDate(int id);
	List<UsersInfo> getUserInfo(Integer userId);
	List<Users> getUser(Integer userId);
	ActdecListedThree getAct3(Integer userId);
	ActdecListedFour getAct4(Integer userId);
	int insertAct3(ActdecListedThree actdecListedThree);
	int insertAct4(ActdecListedFour actdecListedFour);
	int updateAct3(ActdecListedThree actdecListedThree);
	int updateAct4(ActdecListedFour actdecListedFour);
	int getSpreads(Integer userId);
	List<ActdecListedThree> getAct3List(int type);
	List<ActdecListedFour> getAct4List(int type);
	List<ActdecListedOne> getAct1List(Integer userId);
	ActdecListedOne getAct1(Integer userId);
	int insertAct1 (ActdecListedOne one);
	List<BorrowTender> getBorrowTender(String orderId);
	Borrow getBorrow(String borrowNid);
	
}
