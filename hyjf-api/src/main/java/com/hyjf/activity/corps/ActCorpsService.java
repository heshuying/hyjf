package com.hyjf.activity.corps;

import java.util.List;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.ActdecCorps;
import com.hyjf.mybatis.model.auto.ActdecWinning;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;


public interface ActCorpsService extends BaseService{
    
    //查询战队列表
    List<ActdecCorps> getActdecCorps(String opId);
    //增加战队
    int addActdecCorps(String opId,String name,String head);
    //加入战队
    ActdecCorps joinActdecCorps(String id,String opId,String name,String head);
    //查询战队中奖列表
    List<ActdecWinning> getActdecWinning(String corpsId);
    //增加中奖条数
    String addActdecWinning(ActdecWinning aw,int userId,UsersInfo uio);
    //查询用户
    List<Users> getuser(String mob);
    //查询用户信息
    List<UsersInfo> getUserInfo(int userId);
    //查询战队列表
    ActdecCorps getActdecCorpsOne(String id);
    /**
     * 获取活动时间
     * 
     * @return
     */
    public ActivityList getActivityDate(int id);

}
