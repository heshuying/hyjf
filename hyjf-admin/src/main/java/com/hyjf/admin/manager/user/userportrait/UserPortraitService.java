/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.user.userportrait;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.UsersPortrait;
import com.hyjf.mybatis.model.customize.UserPortraitCustomize;

import java.util.List;
import java.util.Map;

/**
 * @author ${yaoy}
 * @version UserPortraitService, v0.1 2018/5/11 14:37
 */
public interface UserPortraitService extends BaseService {
    /**
     * 获取用户画像表的用户数
     * @param userPortrait
     * @return
     */
    int countRecordTotal(Map<String, Object> userPortrait);

    /**
     * 查询用户画像表的所有信息
     * @param userPortrait
     * @param limitStart
     * @param limitEnd
     * @return
     */
    List<UserPortraitCustomize> getRecordList(Map<String, Object> userPortrait, int limitStart, int limitEnd);

    /**
     * 根据用户Id查询用户画像信息
     * @param userId
     * @return
     */
    UsersPortrait getUsersPortraitByUserId(Integer userId);

    /**
     * 保存编辑用户画像信息
     * @param form
     * @return
     */
    Map<String,Object> saveUserPortrait(UsersPortraitBean form);
}
