package com.hyjf.admin.manager.activity.prize;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.customize.app.AppUserPrizeCodeCustomize;
import com.hyjf.mybatis.model.customize.app.AppUserPrizeOpportunityCustomize;

@Service
public class PrizeCodeListServiceImpl extends BaseServiceImpl implements PrizeCodeListService {

    @Override
    public Integer selectRecordCount(Map<String, Object> paraMap) {
        return appUserPrizeCodeCustomizeMapper.selectRecordCount(paraMap);
    }

    @Override
    public List<AppUserPrizeCodeCustomize> selectRecordList(Map<String, Object> paraMap) {
        return appUserPrizeCodeCustomizeMapper.selectRecordList(paraMap);
    }

    @Override
    public Integer selectPrizeOpportunityCount(Map<String, Object> paraMap) {
        return appUserPrizeCodeCustomizeMapper.selectPrizeOpportunityCount(paraMap);
    }

    @Override
    public List<AppUserPrizeOpportunityCustomize> selectPrizeOpportunityList(Map<String, Object> paraMap) {
        return appUserPrizeCodeCustomizeMapper.selectPrizeOpportunityList(paraMap);
    }

    @Override
    public ActivityList getActivityListById(Integer activityId){
        ActivityList activity = this.activityListMapper
                .selectByPrimaryKey(Integer.valueOf(activityId));
        return activity;
    }

}
