package com.hyjf.web.activity.activity518;

import java.util.List;

import com.hyjf.mybatis.model.auto.ActdecFinancing;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.web.BaseService;


public interface Act518Service extends BaseService{


    public ActivityList getActivityDate(int id);
	List<UsersInfo> getUserInfo(Integer userId);
	List<Users> getUser(Integer userId);
	List<ActdecFinancing> getAct518(String userId);
	int insertAct(ActdecFinancing af);
	List<ActdecFinancing> getAct518two(Integer userId);
	
}
