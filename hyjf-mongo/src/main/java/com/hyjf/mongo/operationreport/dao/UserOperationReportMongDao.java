/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.operationreport.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.operationreport.entity.UserOperationReportEntity;
import org.springframework.stereotype.Service;

/**
 *  用户分析报告
 * @author yinhui
 * @version UserOperationReportMongDao, v0.1 2018/6/27 9:59
 */
@Service
public class UserOperationReportMongDao extends BaseMongoDao<UserOperationReportEntity> {
    @Override
    protected Class<UserOperationReportEntity> getEntityClass() {
        return UserOperationReportEntity.class;
    }
}
