/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.web.activity.twoeleven;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.customize.activity.ActivityOtherDataVO;
import com.hyjf.mybatis.model.customize.activity.TwoElevenActivityVO;

import java.util.Map;

/**
 * @author yinhui
 * @version TwoelevenActivityService, v0.1 2018/10/11 10:00
 */
public interface TwoelevenActivityService {

    ActivityList checkActivityIfAvailable(String activityId);

    TwoElevenActivityVO getActivityJX(ActivityList activityList);

    TwoElevenActivityVO getActivityDJ(ActivityList activityList);

    Map<String,Object> getActivitySW(Integer userId, ActivityList activityList);

    ActivityOtherDataVO getActivityOtherDataVO(Integer userId, ActivityList activityList);

    Map<String,Integer> getCouponData(String awardId);

    JSONObject seckillAward(String couponId, String activity, Integer userId);
}
