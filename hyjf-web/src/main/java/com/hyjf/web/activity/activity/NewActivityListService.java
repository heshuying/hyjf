package com.hyjf.web.activity.activity;

import java.util.List;

import com.hyjf.mybatis.model.auto.ActivityList;

public interface NewActivityListService {
    

    /**
     * 获取活动列表列表
     * 
     * @return
     */
    public List<ActivityList> getRecordList(ActivityList borrowActivityList, int limitStart, int limitEnd);
    
    
    /**
     * 根据条件查询数据
     * 
     * @param activityList
     * @param i
     * @param j
     * @return
     */
    public List<ActivityList> selectRecordList(NewActivityListBean form, int limitStart, int limitEnd);
    
}