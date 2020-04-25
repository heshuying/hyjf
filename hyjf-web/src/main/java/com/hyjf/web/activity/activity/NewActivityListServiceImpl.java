package com.hyjf.web.activity.activity;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.ActivityListExample;
import com.hyjf.mybatis.model.customize.ActivityListCustomize;
import com.hyjf.web.BaseServiceImpl;

@Service
public class NewActivityListServiceImpl extends BaseServiceImpl implements NewActivityListService {
    
    

    /**
     * 获取活动列表列表
     * 
     * @return
     */
    public List<ActivityList> getRecordList(ActivityList ActivityList, int limitStart, int limitEnd) {
        ActivityListExample example = new ActivityListExample();

        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        ActivityListExample.Criteria cra = example.createCriteria();
        cra.andPlatformLike("0");
        return activityListMapper.selectByExample(example);
    }
    
    
    
    /**
     * 根据条件查询数据
     */
    public List<ActivityList> selectRecordList(NewActivityListBean form, int limitStart, int limitEnd) {
        ActivityListCustomize example = new ActivityListCustomize();
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        // 获取创建时间
        if (StringUtils.isNotEmpty(form.getStartCreate())) {
            example.setStartCreate(Integer.valueOf(GetDate.get10Time(form.getStartCreate())));
        }
        if (StringUtils.isNotEmpty(form.getEndCreate())) {
            example.setEndCreate(Integer.valueOf(GetDate.get10Time(form.getEndCreate())));
        }
        if (StringUtils.isNotEmpty(form.getStartTime())) {
            example.setStartTime(Integer.valueOf(GetDate.get10Time(form.getStartTime())));
        }
        if (StringUtils.isNotEmpty(form.getEndTime())) {
            example.setEndTime(Integer.valueOf(GetDate.get10Time(form.getEndTime())));
        }
        // 获取活动名
        example.setTitle(form.getTitle());
        return activityListCustomizeMapper.selectActivityList(example);
    }

}
