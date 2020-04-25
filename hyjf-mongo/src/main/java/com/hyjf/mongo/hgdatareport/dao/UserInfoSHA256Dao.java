/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.hgdatareport.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.hgdatareport.entity.UserInfoSHA256Entity;
import org.springframework.stereotype.Repository;

/**
 * @author PC-LIUSHOUYI
 * @version UserInfoSHA256Dao, v0.1 2018/12/7 15:09
 */
@Repository
public class UserInfoSHA256Dao extends BaseMongoDao<UserInfoSHA256Entity> {
    @Override
    protected Class<UserInfoSHA256Entity> getEntityClass() {
        return UserInfoSHA256Entity.class;
    }
}
