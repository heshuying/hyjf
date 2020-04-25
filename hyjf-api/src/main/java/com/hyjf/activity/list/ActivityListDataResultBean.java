package com.hyjf.activity.list;

import java.util.List;

import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.mybatis.model.auto.ActivityList;

public class ActivityListDataResultBean extends BaseResultBean{

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;

    /**
     * 活动列表（进行中）
     */
    List<ActivityList> actListStarting;
    
    /**
     * 活动列表（未开始）
     */
    List<ActivityList> actListWaitingStart;
    
    /**
     * 活动列表（已结束）
     */
    List<ActivityList> actListEnded;

    public List<ActivityList> getActListStarting() {
        return actListStarting;
    }

    public void setActListStarting(List<ActivityList> actListStarting) {
        this.actListStarting = actListStarting;
    }

    public List<ActivityList> getActListWaitingStart() {
        return actListWaitingStart;
    }

    public void setActListWaitingStart(List<ActivityList> actListWaitingStart) {
        this.actListWaitingStart = actListWaitingStart;
    }

    public List<ActivityList> getActListEnded() {
        return actListEnded;
    }

    public void setActListEnded(List<ActivityList> actListEnded) {
        this.actListEnded = actListEnded;
    }
    
    
}
