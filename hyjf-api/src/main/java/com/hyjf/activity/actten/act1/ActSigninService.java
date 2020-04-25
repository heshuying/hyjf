package com.hyjf.activity.actten.act1;

import java.util.List;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.ActRewardList;
import com.hyjf.mybatis.model.auto.ActSignin;
import com.hyjf.mybatis.model.auto.ActivityList;


public interface ActSigninService extends BaseService{

	//查询签到列表
    List<ActSignin> getActSignin(String userid);
    //查询优惠券列表
    List<ActRewardList> getActRewardList(String userid);
    //签到       月份是2位
    boolean setActSignin(String userid,String userName,String mobile);
    //领取优惠券
    boolean setActRewardList(String userid,String userName,String mobile,int type);
    //发放优惠券
    boolean updateActRewardList(String userid,int type,String couponCode,Integer state );
    /**
     * 获取活动时间
     * 
     * @return
     */
    public ActivityList getActivityDate(int id);

}
