/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.app.activity.midautumn;

import com.hyjf.mybatis.model.customize.ActivityMidauInfoCustomize;

import java.util.List;

/**
 * @author yinhui
 * @version MidauActivityService, v0.1 2018/9/8 11:00
 */
public interface MidauActivityService {

    void registerUser(Integer userId,String activityType) throws Exception;

    List<ActivityMidauInfoCustomize> getActivityMidauInfoList(Integer userId);

    String checkActivityIfAvailable(String activityId);
}
