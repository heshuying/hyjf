/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.user.userportraitscore;

import com.hyjf.mybatis.model.customize.admin.UserPortraitScoreCustomize;

import java.util.List;
import java.util.Map;

/**
 * @author yaoyong
 * @version UserPortraitScoreService, v0.1 2018/7/9 17:48
 */
public interface UserPortraitScoreService {
    /**
     * 获取用户数
     * @param userPortraitScore
     * @return
     */
    int countRecordTotal(Map<String, Object> userPortraitScore);

    /**
     * 查询所有信息
     *
     * @param userPortrait
     * @param limitStart
     * @param limitEnd
     * @return
     */
    List<UserPortraitScoreCustomize> getRecordList(Map<String, Object> userPortrait, int limitStart, int limitEnd);
}
