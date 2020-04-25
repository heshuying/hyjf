package com.hyjf.app.activity.newyear2018;

import java.util.List;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.ActRewardList;
import com.hyjf.mybatis.model.auto.ActSignin;
import com.hyjf.mybatis.model.auto.ActdecSpringList;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;


public interface SpringActService extends BaseService{


    /**
     * 获取活动时间
     * 
     * @return
     */
    public ActivityList getActivityDate(int id);
	List<UsersInfo> getUserInfo(Integer userId);
	List<Users> getUser(Integer userId);

	boolean addActdecSpringList(ActdecSpringList actdecSpringList);
	ActdecSpringList getActdecSpringList(String userId);
	List<ActdecSpringList> getActdecSpringLists(String userId);
	List<ActdecSpringList> getZhongJiang(String userId);
	Account getAccount(int userId);
	
}
