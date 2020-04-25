package com.hyjf.admin.manager.activity.prize;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.customize.app.AppUserPrizeCodeCustomize;
import com.hyjf.mybatis.model.customize.app.AppUserPrizeOpportunityCustomize;

public interface PrizeCodeListService extends BaseService {

    Integer selectRecordCount(Map<String, Object> paraMap);

    List<AppUserPrizeCodeCustomize> selectRecordList(Map<String, Object> paraMap);

    Integer selectPrizeOpportunityCount(Map<String, Object> paraMap);

    List<AppUserPrizeOpportunityCustomize> selectPrizeOpportunityList(Map<String, Object> paraMap);

    ActivityList getActivityListById(Integer activityId);

    
}
