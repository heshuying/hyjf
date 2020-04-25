/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.operationlog.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import org.springframework.stereotype.Repository;

/**
 * @author yaoyong
 * @version UserOperationLogDao, v0.1 2018/10/9 9:20
 */
@Repository
public class UserOperationLogDao extends BaseMongoDao<UserOperationLogEntity> {
    @Override
    protected Class<UserOperationLogEntity> getEntityClass() {
        return UserOperationLogEntity.class;
    }
}
