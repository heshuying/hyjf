package com.hyjf.web.activity.activity;

import java.util.List;

import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.web.WebBaseAjaxResultBean;

public class NewActivityListResultBean extends WebBaseAjaxResultBean {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -7274694682649646576L;

    private List<ActivityList> ActivityListBeanList;

    public List<ActivityList> getActivityListBeanList() {
        return ActivityListBeanList;
    }

    public void setActivityListBeanList(List<ActivityList> activityListBeanList) {
        ActivityListBeanList = activityListBeanList;
    }


}
