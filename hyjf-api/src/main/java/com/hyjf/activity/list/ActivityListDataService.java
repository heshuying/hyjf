package com.hyjf.activity.list;

import java.util.List;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.ActivityList;

public interface ActivityListDataService extends BaseService{

    List<ActivityList> getActivityListEnded(Integer count, String... platform);

    List<ActivityList> getActivityListWaitingStart(String... platform);

    List<ActivityList> getActivityListStarting(String... platform);

}
