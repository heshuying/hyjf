package com.hyjf.activity.actdec2018.listact;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.*;

import java.util.List;


public interface ListActService extends BaseService{


    /**
     * 获取活动时间
     * 
     * @return
     */
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
	int insertAct1(ActdecListedOne one);
	int updateAct1(ActdecListedOne one);
	List<BorrowTender> getBorrowTender(String orderId);
	Borrow getBorrow(String borrowNid);
	int getAct1Count(String borrowNid);
	Boolean insertMoney(Integer userId,String trueName);
}
