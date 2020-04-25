package com.hyjf.app.activity.activity68;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.ActivityList;

/**
 * 
 * 注册送68元代金券活动Service
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月25日
 * @see 下午2:36:36
 */
public interface Activity68Service extends BaseService {

    ActivityList getActivityListById(String activityId);
}
