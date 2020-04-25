package com.hyjf.admin.manager.activity.newuser;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.customize.admin.AdminNewUserActivityCustomize;

@Service
public class NewUserActivityServiceImpl extends BaseServiceImpl implements NewUserActivityService {

    @Override
    public Integer selectRecordCount(Map<String, Object> paraMap) {
        return adminNewUserActivityCustomizeMapper.selectRecordCount(paraMap);
    }

    @Override
    public List<AdminNewUserActivityCustomize> selectRecordList(Map<String, Object> paraMap) {
        return adminNewUserActivityCustomizeMapper.selectRecordList(paraMap);
    }
    
    @Override
    public Integer selectRegistAllCount(Map<String, Object> paraMap) {
        return adminNewUserActivityCustomizeMapper.selectRegistAllCount(paraMap);
    }

    @Override
    public List<AdminNewUserActivityCustomize> selectRegistAllList(Map<String, Object> paraMap) {
        return adminNewUserActivityCustomizeMapper.selectRegistAllList(paraMap);
    }

    /**
     * 
     * 根据id获取活动
     * @author hsy
     * @param activityId
     * @return
     * @see com.hyjf.admin.manager.activity.newuser.NewUserActivityService#getActivityListById(java.lang.Integer)
     */
    @Override
    public ActivityList getActivityListById(Integer activityId){
        ActivityList activity = this.activityListMapper
                .selectByPrimaryKey(Integer.valueOf(activityId));
        return activity;
    }

}
