package com.hyjf.activity.list;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.ActivityListExample;

@Service
public class ActivityListDataServiceImpl extends BaseServiceImpl implements ActivityListDataService{
    
    /**
     * 
     * 获取活动列表（进行中）
     * @author hsy
     * @return
     */
    @Override
    public List<ActivityList> getActivityListStarting(String... platform){
        ActivityListExample example = new ActivityListExample();
        
        for(int i=0; i<platform.length; i++){
            example.or().andTimeStartLessThanOrEqualTo(GetDate.getNowTime10())
            .andTimeEndGreaterThanOrEqualTo(GetDate.getNowTime10()).andPlatformLike("%" + platform[i] + "%");
        }
        example.setOrderByClause(" time_start desc ");
        
        List<ActivityList> result = activityListMapper.selectByExample(example);
        if(result == null){
            result = new ArrayList<ActivityList>();
        }
        
        return result;
    }
    
    /**
     * 
     * 获取活动列表（未开始）
     * @author hsy
     * @return
     */
    @Override
    public List<ActivityList> getActivityListWaitingStart(String... platform){
        ActivityListExample example = new ActivityListExample();
        
        for(int i=0; i<platform.length; i++){
            example.or().andTimeStartGreaterThanOrEqualTo(GetDate.getNowTime10()).andPlatformLike("%" + platform[i] + "%");
        }
        example.setOrderByClause(" time_start asc ");
        
        List<ActivityList> result = activityListMapper.selectByExample(example);
        
        if(result == null){
            result = new ArrayList<ActivityList>();
        }
        
        return result;
        
    }
    
    /**
     * 
     * 获取活动列表（已结束）
     * @author hsy
     * @return
     */
    @Override
    public List<ActivityList> getActivityListEnded(Integer count, String... platform){
        ActivityListExample example = new ActivityListExample();
        
        for(int i=0; i<platform.length; i++){
            example.or().andTimeEndLessThanOrEqualTo(GetDate.getNowTime10()).andPlatformLike("%" + platform[i] + "%");
        }
        
        example.setOrderByClause(" time_end desc ");
        example.setLimitStart(0);
        example.setLimitEnd(count);
        
        List<ActivityList> result = activityListMapper.selectByExample(example);
        
        if(result == null){
            result = new ArrayList<ActivityList>();
        }
        
        return result;
    }
}
