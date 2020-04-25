package com.hyjf.wechat.controller.activity.listtwo2018;



import java.util.List;

import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.ActdecListedTwo;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.wechat.base.BaseService;


public interface ListedTwoActService extends BaseService{


    /**
     * 获取活动时间
     * 
     * @return
     */
    public ActivityList getActivityDate(int id);
    /**
     * 获取用户详细信息
     * 
     * @param userId
     * @return
     */
    public List<UsersInfo> getUserInfo(Integer userId);
	/**
	 * 获取用户信息
	 * 
	 * @param userId
	 * @return
	 */
    public List<Users> getUser(Integer userId);
	
	/**
	 * 冲投表插入一条新记录
	 * 
	 * @param actdecListedTwo
	 * @return
	 */
    public boolean addActdecSpringList(ActdecListedTwo actdecListedTwo);
	
	/**
	 * 获取用户当前冲投、领奖信息
	 * 
	 * @param userId
	 * @return
	 */
    public ActdecListedTwo getActdecListedTwoList(String userId);
	
	/**
	 * 查询用户是否有冲投记录
	 * 
	 * @param userId
	 * @return
	 */
//	List<ActdecListedTwoCustomize> getActdecSpringLists(String userId);
	/**
	 * ???
	 * 
	 * @param userId
	 * @return
	 */
//	List<ActdecListedTwoCustomize> getZhongJiang(String userId);
	/**
	 * 获取用户账户信息
	 * 
	 * @param userId
	 * @return
	 */
    public Account getAccount(int userId);
	
}
