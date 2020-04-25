package com.hyjf.app.activity.activity68;

import org.springframework.stereotype.Service;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.ActivityList;

@Service
public class Activity68ServiceImpl extends BaseServiceImpl implements Activity68Service {

    @Override
    public ActivityList getActivityListById(String activityId) {
        // TODO Auto-generated method stub
        return activityListMapper.selectByPrimaryKey(new Integer(activityId));
    }
    
}
