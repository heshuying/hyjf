/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.web.activity.midautumn;

import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.ActivityMidauInfo;
import com.hyjf.mybatis.model.auto.ActivityMidauInfoExample;
import com.hyjf.mybatis.model.customize.ActivityMidauInfoCustomize;
import com.hyjf.web.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yinhui
 * @version MidauActivityServiceImpl, v0.1 2018/9/8 11:00
 */
@Service("webMidauActivityServiceImpl")
public class MidauActivityServiceImpl extends BaseServiceImpl implements MidauActivityService {

    @Override
    public void registerUser(Integer userId,String activityType) throws Exception{

        if(userId == null || StringUtils.isEmpty(activityType)){
            throw new  NullPointerException("入参为空");
        }

        //更新redis,存储30天
        RedisUtils.set(RedisConstants.MIDAU_ACTIVITY_USER_+userId,activityType,2592000);

    }

    @Override
    public List<ActivityMidauInfoCustomize> getActivityMidauInfoList(Integer userId){

        List<ActivityMidauInfoCustomize> infoCustomizeList = new ArrayList<>();

        ActivityMidauInfoExample example = new ActivityMidauInfoExample();
        ActivityMidauInfoExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        example.setOrderByClause(" create_time DESC ");

        List<ActivityMidauInfo> list = activityMidauInfoMapper.selectByExample(example);
        int i =1;
        for(ActivityMidauInfo info : list){
            ActivityMidauInfoCustomize midacustomize = new ActivityMidauInfoCustomize();
            BeanUtils.copyProperties(info,midacustomize);
            midacustomize.setIds(i);
            infoCustomizeList.add(midacustomize);
            i++;
        }

        return infoCustomizeList;
    }

    /**
     * 查询活动是否开始
     * @param activityId
     * @return
     */
    @Override
    public String checkActivityIfAvailable(String activityId) {
        if (activityId == null) {
            return "103";
        }
        ActivityList activityList = activityListMapper.selectByPrimaryKey(new Integer(activityId));
        if (activityList == null) {
            return "104";
        }
        if (activityList.getTimeStart() > GetDate.getNowTime10()) {
            return "101";
        }
        if (activityList.getTimeEnd() < GetDate.getNowTime10()) {
            return "102";
        }
        return "000";
    }
}
