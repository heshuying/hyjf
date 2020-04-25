/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.user.portrait;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.UsersPortrait;

import java.util.List;

/**
 * @author ${yaoy}
 * @version OntimeUserPortraitService, v0.1 2018/5/9 11:55
 */
public interface OntimeUserPortraitService extends BaseService {

    /**
     * 查询前一天登录的所有用户
     * @return
     */
    List<UsersPortrait> selcetUserList();

    /**
     * 更新用户画像信息
     * @param usersPortrait
     * @return
     */

    int updateImformation(UsersPortrait usersPortrait);

    /**
     * 插入用户画像信息
     * @param usersPortrait
     * @return
     */
    int insertImformation(UsersPortrait usersPortrait);
}
