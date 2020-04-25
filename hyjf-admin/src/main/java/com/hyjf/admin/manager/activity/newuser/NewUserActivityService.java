package com.hyjf.admin.manager.activity.newuser;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.customize.admin.AdminNewUserActivityCustomize;

public interface NewUserActivityService extends BaseService {

    Integer selectRecordCount(Map<String, Object> paraMap);

    List<AdminNewUserActivityCustomize> selectRecordList(Map<String, Object> paraMap);

    ActivityList getActivityListById(Integer activityId);

    Integer selectRegistAllCount(Map<String, Object> paraMap);

    List<AdminNewUserActivityCustomize> selectRegistAllList(Map<String, Object> paraMap);

    
}
